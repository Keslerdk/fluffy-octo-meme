package org.example.detekt.arch

import io.github.detekt.psi.absolutePath
import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.com.intellij.psi.PsiFile

enum class ModuleType(val possibleNames: List<String>) {

    DATA(listOf("data", "repository")),
    DOMAIN(listOf("domain")),
    UI(listOf("ui", "presentation"));
}

class DependencyRule(config: Config) : Rule(config) {
    companion object {
        const val RULE_DESCRIPTION = "This rule reports the module which has incorrect dependencies"
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.CodeSmell, RULE_DESCRIPTION, Debt.TWENTY_MINS)


    override fun visitFile(file: PsiFile) {
        super.visitFile(file)
        if (file.name.contains(".gradle") || file.name.contains(".kts")) {
            checkDependencies(file)
        }
    }

    private fun checkDependencies(file: PsiFile) {
        ModuleType.entries.forEach { type ->
            val isLayer = type.possibleNames.any { name ->
                file.absolutePath().toString().split('/').any { it == name }
            }
            if (isLayer) {
                val forbiddenDeps = getForbiddenDeps(type)
                val regexList = forbiddenDeps.map { getRegex(it) }
                if (regexList.any { it.containsMatchIn(file.text) }) {
                    report(
                        CodeSmell(
                            issue,
                            Entity.Companion.from(file),
                            getReportMessage(type, forbiddenDeps)
                        )
                    )
                }
            }
        }
    }

    private fun getForbiddenDeps(moduleType: ModuleType) = when (moduleType) {
        ModuleType.DATA -> listOf(ModuleType.UI)
        ModuleType.DOMAIN -> listOf(ModuleType.DATA, ModuleType.UI)
        ModuleType.UI -> listOf(ModuleType.DATA)
    }

    private fun getRegex(moduleType: ModuleType): Regex {
        return Regex(
            pattern = ".*(implementation|api)\\(.*(${moduleType.possibleNames.joinToString("|")}).*",
            options = setOf(RegexOption.IGNORE_CASE)
        )
    }

    private fun getReportMessage(type: ModuleType, forbiddenDeps: List<ModuleType>) =
        "$type module shouldn't depends from ${forbiddenDeps.joinToString()} "
}

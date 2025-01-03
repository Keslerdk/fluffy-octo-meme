package org.example.detekt.rules.arch.modules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.example.detekt.rules.arch.ModuleType
import org.example.detekt.rules.getCurrentLayer
import org.jetbrains.kotlin.com.intellij.psi.PsiFile

class ModuleDependencyRule(config: Config) : Rule(config) {
    companion object {
        const val RULE_DESCRIPTION =
            "Проверяет корректность зависимостей между модулями 'data', 'domain' и 'ui'."
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
        getCurrentLayer(file.name)?.let { type ->
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
        "Модуль $type не должен зависеть от ${forbiddenDeps.joinToString()} "
}

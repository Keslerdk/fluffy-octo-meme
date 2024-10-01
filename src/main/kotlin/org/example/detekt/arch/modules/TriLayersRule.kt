package org.example.detekt.arch.modules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.example.detekt.arch.ModuleType
import org.jetbrains.kotlin.com.intellij.psi.PsiFile

class TriLayersRule(config: Config) : Rule(config) {

    private companion object {
        const val RULE_DESCRIPTION = "This module reports if there is no data, domain or ui layer"

        const val SETTINGS_FILE = "settings.gradle.kts"
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.CodeSmell, RULE_DESCRIPTION, Debt.TEN_MINS)

    override fun visitFile(file: PsiFile) {
        super.visitFile(file)
        if (file.name.contains(SETTINGS_FILE)) {
            checkLayers(file)
        }
    }

    private fun checkLayers(file: PsiFile) {
        ModuleType.entries.map { type ->
            val noLayer =
                type.possibleNames.none { file.text.contains(it, ignoreCase = true) }
            if (noLayer) {
                report(CodeSmell(issue, Entity.Companion.from(file), getRegexText(type)))
            }
        }
    }

    private fun getRegexText(type: ModuleType) = "There is no $type layer in project"
}

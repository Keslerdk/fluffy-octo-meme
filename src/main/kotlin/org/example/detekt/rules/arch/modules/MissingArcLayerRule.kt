package org.example.detekt.rules.arch.modules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.example.detekt.rules.arch.ModuleType
import org.jetbrains.kotlin.com.intellij.psi.PsiFile

class MissingArcLayerRule(config: Config) : Rule(config) {

    private companion object {
        const val RULE_DESCRIPTION = "Проверяет, что в проекте присутствуют слои 'data', 'domain' и 'ui'"

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

    private fun getRegexText(type: ModuleType) = "В проекте отсутствует слой: $type"
}

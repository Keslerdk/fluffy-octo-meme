package org.example.detekt.compose

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.getParentOfType

class NoDirectColorUseRule(config: Config) : Rule(config) {

    companion object {
        private const val RULE_DESCRIPTION =
            "Проверяет, что цвета не создаются напрямую через Color(), а используются из темы."
        private const val REPORT_MESSAGE =
            "Используйте цвета из темы приложения"
    }

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Defect,
        description = RULE_DESCRIPTION,
        debt = Debt.FIVE_MINS
    )

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)

        if (isColorsClass(expression) == true) return

        if (expression.calleeExpression?.text == "Color") {
            report(CodeSmell(issue, Entity.from(expression), REPORT_MESSAGE))
        }
    }

    override fun visitDotQualifiedExpression(expression: KtDotQualifiedExpression) {
        super.visitDotQualifiedExpression(expression)

        if (isColorsClass(expression) == true) return

        // Проверка на использование цветов через Color.Red, Color.Blue и т.д.
        if (expression.text.startsWith("Color.")) {
            report(CodeSmell(issue, Entity.from(expression), REPORT_MESSAGE))
        }
    }

    private fun isColorsClass(element: PsiElement) =
        element.getParentOfType<KtClass>(true)?.name?.contains("Colors", ignoreCase = true)
}

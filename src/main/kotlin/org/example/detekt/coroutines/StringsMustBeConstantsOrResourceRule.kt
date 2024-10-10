package org.example.detekt.coroutines

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.rules.isConstant
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.psiUtil.getParentOfType

class StringsMustBeConstantsOrResourceRule(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION =
            "Проверяет, что все строки в коде объявляются как константы и не используются напрямую."
        const val REPORT_MESSAGE = " Объявите строку как константу или вынесети в ресурсы."
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.Defect, RULE_DESCRIPTION, Debt.FIVE_MINS)

    override fun visitStringTemplateExpression(expression: KtStringTemplateExpression) {
        super.visitStringTemplateExpression(expression)
        if ((expression.parent as? KtProperty)?.isConstant() == false &&
            !isGradleFile(expression) && !isTestFile(expression)
        ) {
            report(CodeSmell(issue, Entity.from(expression), REPORT_MESSAGE))
        }
    }

    override fun visitCallExpression(expression: KtCallExpression) {
        // Проверяем аргументы вызова функций
        if (isGradleFile(expression) || isTestFile(expression)) return

        expression.valueArguments.forEach { argument ->
            expression.parent
            val argumentExpression = argument.getArgumentExpression()
            if (argumentExpression is KtStringTemplateExpression
                && !isExceptionConstructor(argumentExpression)
            ) {
                report(CodeSmell(issue, Entity.from(argumentExpression), REPORT_MESSAGE))
            }
        }
        super.visitCallExpression(expression)
    }

    private fun isExceptionConstructor(expression: KtStringTemplateExpression): Boolean {
        // Проверяем, является ли строка частью конструктора исключения
        val callExpression = expression.getParentOfType<KtCallExpression>(true)
        return callExpression?.let {
            val calleeExpression = it.calleeExpression as? KtNameReferenceExpression
            val parentType = it.getParentOfType<KtClassOrObject>(true)

            // Проверяем, является ли родительский класс исключением
            parentType != null && (calleeExpression?.text?.contains("Exception") == true)
        } ?: false
    }

    private fun isGradleFile(element: PsiElement) =
        element.getParentOfType<KtFile>(true)?.name?.contains("gradle", ignoreCase = true) == true

    private fun isTestFile(element: PsiElement) =
        element.getParentOfType<KtFile>(true)?.name?.contains("test", ignoreCase = true) == true

}

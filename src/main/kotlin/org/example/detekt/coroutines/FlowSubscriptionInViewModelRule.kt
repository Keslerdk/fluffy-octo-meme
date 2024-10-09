package org.example.detekt.coroutines

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.forEachDescendantOfType
import org.jetbrains.kotlin.psi.psiUtil.getParentOfType

class FlowSubscriptionInViewModelRule(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION =
            "Проверяет, что подписка на Flow происходит только в ViewModel."
        const val REPORT_MESSAGE = "Подписка на Flow возможна только в ViewModel"
        val subscribeFunction =
            listOf(
                "collect(\\(|\\{).*(\\)|\\})".toRegex(),
                "launchIn\\(.*\\)".toRegex()
            )
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.Warning, RULE_DESCRIPTION, Debt.TEN_MINS)


    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        val isViewModel = klass.name?.contains("ViewModel", ignoreCase = true) == true
        if (!isViewModel) {
            klass.body?.functions?.forEach { function ->
                checkFunctionForCollect(function)
            }
        }
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        val isSeparateFunc = function.getParentOfType<KtClass>(true) == null
        if (isSeparateFunc) {
            checkFunctionForCollect(function)
        }
    }

    private fun checkFunctionForCollect(function: KtNamedFunction) {
        val body = function.bodyBlockExpression ?: function.bodyExpression
        body?.forEachDescendantOfType<KtDotQualifiedExpression> { expression ->
            val isSubscribed = subscribeFunction.any {
                expression.selectorExpression?.text?.matches(it) == true
            }
            if (isSubscribed) {
                report(CodeSmell(issue, Entity.from(expression), REPORT_MESSAGE))
            }
        }
    }
}

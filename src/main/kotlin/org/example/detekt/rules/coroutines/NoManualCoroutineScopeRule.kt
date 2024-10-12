package org.example.detekt.rules.coroutines

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtCallExpression

class NoManualCoroutineScopeRule(config: Config) : Rule(config) {

    private companion object {
        const val RULE_DESCRIPTION = "Проверяет, что CoroutineScope не создается вручную."
    }

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Defect,
        description = RULE_DESCRIPTION,
        debt = Debt.TEN_MINS
    )

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)

        if (expression.calleeExpression?.text == "CoroutineScope") {
            report(
                CodeSmell(
                    issue,
                    Entity.from(expression),
                    "Используйте встроенные скоупы (viewModelScope, lifecycleScope)"
                )
            )
        }
    }
}

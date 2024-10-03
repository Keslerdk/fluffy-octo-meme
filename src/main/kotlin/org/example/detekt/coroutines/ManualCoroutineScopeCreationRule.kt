package org.example.detekt.coroutines

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtCallExpression

class ManualCoroutineScopeCreationRule(config: Config) : Rule(config) {

    private companion object {
        const val RULE_DESCRIPTION =
            "Not allowed to create CoroutineScope. Use scope with lifecycles (such as viewModelScope, lifecycleScope)"
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
                    "Use scope with lifecycle (such as viewModelScope, lifecycleScope)"
                )
            )
        }
    }
}

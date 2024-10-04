package org.example.detekt.compose

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.*

class NoCollectAsStateInComposableRule(config: Config) : Rule(config) {

    private companion object {
        const val RULE_DESCRIPTION = "Compose function not allowed collectAsState"
        const val REPORT_MESSAGE = "collectAsState not allowed, use collectAsStateWithLifecycle"
    }

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Defect,
        description = RULE_DESCRIPTION,
        debt = Debt.FIVE_MINS
    )

    override fun visitDotQualifiedExpression(expression: KtDotQualifiedExpression) {
        super.visitDotQualifiedExpression(expression)
        if (expression.selectorExpression?.text == "collectAsState()") {
            report(CodeSmell(issue, Entity.from(expression), REPORT_MESSAGE))
        }
    }
}

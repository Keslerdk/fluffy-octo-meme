package org.example.detekt.rules.compose

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.*

class NoCollectAsStateRule(config: Config) : Rule(config) {

    private companion object {
        const val RULE_DESCRIPTION = "Проверяет, что collectAsState не используется."
        const val REPORT_MESSAGE = "Замените collectAsState на collectAsStateWithLifecycle"
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

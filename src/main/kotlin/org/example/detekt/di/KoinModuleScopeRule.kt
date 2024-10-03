package org.example.detekt.di

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.*

class KoinModuleScopeRule(config: Config) : Rule(config) {

    private companion object {
        const val RULE_DESCRIPTION = "Koin modules should contain scope"
    }

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Minor,
        description = RULE_DESCRIPTION,
        debt = Debt.FIVE_MINS
    )

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)

        // Проверяем, является ли это вызовом модуля Koin (module {})
        if (expression.calleeExpression?.text == "module") {
            val moduleBody =
                expression.lambdaArguments.firstOrNull()?.getLambdaExpression()?.bodyExpression

            // Проверяем наличие скоупа внутри модуля
            val hasScope = moduleBody?.statements?.any { it.text.contains("scope") } == true

            if (!hasScope) {
                report(
                    CodeSmell(
                        issue,
                        Entity.from(expression),
                        RULE_DESCRIPTION
                    )
                )
            }
        }
    }
}

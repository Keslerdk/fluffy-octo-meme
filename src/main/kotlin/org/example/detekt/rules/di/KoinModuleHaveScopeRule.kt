package org.example.detekt.rules.di

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.*

class KoinModuleHaveScopeRule(config: Config) : Rule(config) {

    private companion object {
        const val RULE_DESCRIPTION =
            "Проверяет, что в каждом Koin модуле объявлен scope для управления временем жизни зависимостей."
        const val REPORT_MESSAGE = "Koin модуль не содержит объявления scope."
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
                report(CodeSmell(issue, Entity.from(expression), REPORT_MESSAGE))
            }
        }
    }
}

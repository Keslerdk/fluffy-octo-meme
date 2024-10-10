package org.example.detekt.navigation

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.rules.hasAnnotation
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtNamedFunction

class NavigationWithSingleArgumentRule(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION = "Проверяет, что при навигации передается не более одного параметра."
        const val REPORT_MESSAGE = "При навигации следует передавать только один параметр."
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.Performance, RULE_DESCRIPTION, Debt.FIVE_MINS)

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        val isRouteClass = klass.name?.contains("Route", ignoreCase = true) == true
                && klass.hasAnnotation("Serializable")
        val haveManyParams = klass.primaryConstructor?.valueParameters.orEmpty().count() > 1
        if (isRouteClass && haveManyParams) {
            report(CodeSmell(issue, Entity.Companion.from(klass), RULE_DESCRIPTION))
        }
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        val isRouteFunc = function.name?.contains("route", ignoreCase = true) ?: true
        val haveManyParams = function.valueParameters.count() > 1

        if (isRouteFunc && haveManyParams) {
            report(CodeSmell(issue, Entity.Companion.from(function), REPORT_MESSAGE))
        }
    }
}

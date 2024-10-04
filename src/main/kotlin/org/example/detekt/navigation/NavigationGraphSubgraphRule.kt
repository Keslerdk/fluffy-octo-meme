package org.example.detekt.navigation

import io.gitlab.arturbosch.detekt.api.*
import org.example.detekt.body
import org.jetbrains.kotlin.psi.*

class NavigationGraphSubgraphRule(config: Config) : Rule(config) {

    companion object {
        const val RULE_DESCRIPTION = "Navigation should be divided into graphs and subgraphs."
        const val REPORT_MESSAGE = "The navigation graph should be divided into subgraphs."
    }

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Defect,
        description = RULE_DESCRIPTION,
        debt = Debt.FIVE_MINS
    )

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        val isNavHost = function.body?.text?.contains("NavHost", ignoreCase = true) ?: return
        val haveSubgraph = function.body?.text?.contains("navigation") == false
        if (isNavHost && haveSubgraph) {
            report(CodeSmell(issue, Entity.Companion.from(function), REPORT_MESSAGE))
        }
    }
}

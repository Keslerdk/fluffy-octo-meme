package org.example.detekt.rules.navigation

import io.gitlab.arturbosch.detekt.api.*
import org.example.detekt.rules.body
import org.jetbrains.kotlin.psi.*

class NavigationGraphsMustBeDividedIntoSubgraphsRule(config: Config) : Rule(config) {

    companion object {
        const val RULE_DESCRIPTION = "Проверяет, что все навигационные графы разделены на сабграфы."
        const val REPORT_MESSAGE = "Рекомендуется разделить граф на сабграфы."
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

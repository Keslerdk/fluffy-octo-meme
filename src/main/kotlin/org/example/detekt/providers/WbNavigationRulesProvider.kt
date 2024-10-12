package org.example.detekt.providers

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import org.example.detekt.rules.navigation.NavigationGraphsMustBeDividedIntoSubgraphsRule
import org.example.detekt.rules.navigation.NavigationWithSingleArgumentRule

class WbNavigationRulesProvider : RuleSetProvider {
    override val ruleSetId: String
        get() = "WbNavigation"

    override fun instance(config: Config): RuleSet = RuleSet(
        id = ruleSetId,
        rules = listOf(
            //navigation
            NavigationGraphsMustBeDividedIntoSubgraphsRule(config),
            NavigationWithSingleArgumentRule(config)
        )
    )
}

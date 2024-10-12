package org.example.detekt.providers

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import org.example.detekt.rules.coroutines.FlowSubscriptionInViewModelRule
import org.example.detekt.rules.coroutines.NoManualCoroutineScopeRule

class WbCoroutinesRulesProvider : RuleSetProvider {
    override val ruleSetId: String
        get() = "WbCoroutines"

    override fun instance(config: Config): RuleSet = RuleSet(
        id = ruleSetId,
        rules = listOf(
            //coroutines
            NoManualCoroutineScopeRule(config),
            FlowSubscriptionInViewModelRule(config)
        )
    )
}

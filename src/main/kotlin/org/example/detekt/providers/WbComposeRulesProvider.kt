package org.example.detekt.providers

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import org.example.detekt.rules.compose.NoCollectAsStateRule
import org.example.detekt.rules.compose.NoDirectColorUseRule
import org.example.detekt.rules.coroutines.StringsMustBeConstantsOrResourceRule

class WbComposeRulesProvider : RuleSetProvider {
    override val ruleSetId: String
        get() = "WbCompose"

    override fun instance(config: Config): RuleSet = RuleSet(
        id = ruleSetId,
        rules = listOf(//compose
            NoDirectColorUseRule(config),
            NoCollectAsStateRule(config),
            StringsMustBeConstantsOrResourceRule(config)
        )
    )
}

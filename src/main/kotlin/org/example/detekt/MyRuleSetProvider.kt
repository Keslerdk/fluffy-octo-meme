package org.example.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import org.example.detekt.arch.DependencyRule

class MyRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = "rules"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(DependencyRule(config))
        )
    }
}

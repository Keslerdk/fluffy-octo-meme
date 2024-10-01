package org.example.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import org.example.detekt.arch.modules.DependencyRule
import org.example.detekt.arch.modules.ImplLeakRule
import org.example.detekt.arch.modules.TriLayersRule

class MyRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = "rules"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                // modules
                TriLayersRule(config),
                DependencyRule(config),
                ImplLeakRule(config)
            )
        )
    }
}

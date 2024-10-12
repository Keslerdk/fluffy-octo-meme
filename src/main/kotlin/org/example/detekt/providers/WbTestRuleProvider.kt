package org.example.detekt.providers

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import org.example.detekt.rules.test.BeforeAnnotationInTestRule
import org.example.detekt.rules.test.RecreateSUTForEachTestRule
import org.example.detekt.rules.test.StubsNotMocksInTestsRule

class WbTestRuleProvider : RuleSetProvider {
    override val ruleSetId: String
        get() = "WbTests"

    override fun instance(config: Config): RuleSet = RuleSet(
        id = ruleSetId,
        rules = listOf(
            //tests
            StubsNotMocksInTestsRule(config),
            RecreateSUTForEachTestRule(config),
            BeforeAnnotationInTestRule(config)
        )
    )
}

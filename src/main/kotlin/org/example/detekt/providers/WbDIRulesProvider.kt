package org.example.detekt.providers

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import org.example.detekt.rules.di.KoinModuleHaveScopeRule
import org.example.detekt.rules.di.NoDependencyInjectionWithByInjectRule
import org.example.detekt.rules.di.NoKoinModuleInAppModuleRule

class WbDIRulesProvider : RuleSetProvider {
    override val ruleSetId: String
        get() = "WbDI"

    override fun instance(config: Config): RuleSet = RuleSet(
        id = ruleSetId,
        rules = listOf(
            //di
            NoKoinModuleInAppModuleRule(config),
            NoDependencyInjectionWithByInjectRule(config),
            KoinModuleHaveScopeRule(config),
        )
    )
}

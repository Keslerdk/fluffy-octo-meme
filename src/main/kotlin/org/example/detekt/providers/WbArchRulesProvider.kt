package org.example.detekt.providers

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import org.example.detekt.rules.arch.domain.SingleMethodInUseCaseRule
import org.example.detekt.rules.arch.domain.UseCaseNoRepositoryDependencyRule
import org.example.detekt.rules.arch.mappers.ModelMappingRule
import org.example.detekt.rules.arch.mappers.SeparateDataModelPerLayerRule
import org.example.detekt.rules.arch.modules.ImplementationIsolationRule
import org.example.detekt.rules.arch.modules.MissingArcLayerRule
import org.example.detekt.rules.arch.modules.ModuleDependencyRule
import org.example.detekt.rules.arch.mvvm.ViewStateClassRule
import org.example.detekt.rules.arch.mvvm.ViewStateUpdateThroughUpdateMethodRule
import org.example.detekt.rules.arch.repository.NoStateInRepositoryImplRule
import org.example.detekt.rules.arch.repository.RepositoryMethodNamingRule
import org.example.detekt.rules.arch.repository.SingleSourceRepositoryImplRule
import org.example.detekt.rules.data.RepositoryReturnsResultInWrapperRule

class WbArchRulesProvider : RuleSetProvider {
    override val ruleSetId: String
        get() = "WbArch"

    override fun instance(config: Config): RuleSet = RuleSet(
        id = ruleSetId,
        rules = listOf(
            // modules
            MissingArcLayerRule(config),
            ModuleDependencyRule(config),
            ImplementationIsolationRule(config),

            //mappers
            SeparateDataModelPerLayerRule(config),
            ModelMappingRule(config),

            //repository
            NoStateInRepositoryImplRule(config),
            RepositoryMethodNamingRule(config),
            SingleSourceRepositoryImplRule(config),
            RepositoryReturnsResultInWrapperRule(config),

            //domain
            SingleMethodInUseCaseRule(config),
            UseCaseNoRepositoryDependencyRule(config),

            //mvvm
            ViewStateUpdateThroughUpdateMethodRule(config),
            ViewStateClassRule(config),
        )
    )
}

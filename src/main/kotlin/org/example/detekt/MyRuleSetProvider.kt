package org.example.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import org.example.detekt.arch.domain.UseCaseNoRepositoryDependencyRule
import org.example.detekt.arch.domain.SingleMethodInUseCaseRule
import org.example.detekt.arch.mappers.SeparateDataModelPerLayerRule
import org.example.detekt.arch.mappers.ModelMappingRule
import org.example.detekt.arch.modules.ModuleDependencyRule
import org.example.detekt.arch.modules.ImplementationIsolationRule
import org.example.detekt.arch.modules.MissingArcLayerRule
import org.example.detekt.arch.mvvm.ViewStateClassRule
import org.example.detekt.arch.mvvm.ViewStateUpdateThroughUpdateMethodRule
import org.example.detekt.arch.repository.RepositoryMethodNamingRule
import org.example.detekt.arch.repository.NoStateInRepositoryImplRule
import org.example.detekt.arch.repository.SingleSourceRepositoryImplRule
import org.example.detekt.compose.NoCollectAsStateInComposableRule
import org.example.detekt.compose.NoDirectColorUseRule
import org.example.detekt.coroutines.FlowSubscriptionInViewModelRule
import org.example.detekt.coroutines.NoManualCoroutineScopeRule
import org.example.detekt.data.RepositoryReturnsResultInWrapperRule
import org.example.detekt.di.KoinModuleScopeRule
import org.example.detekt.di.KoinModulesDistributionRule
import org.example.detekt.di.NoByInjectRule
import org.example.detekt.navigation.NavigationGraphSubgraphRule
import org.example.detekt.navigation.NoExtraParamsInNavigationRule
import org.example.detekt.test.BeforeAnnotationInTest
import org.example.detekt.test.RecreateSutRule
import org.example.detekt.test.UseStubsInTestsRule

class MyRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = "WBCustomRules"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
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

                //coroutines
                NoManualCoroutineScopeRule(config),
                FlowSubscriptionInViewModelRule(config),

                //di
                KoinModulesDistributionRule(config),
                NoByInjectRule(config),
                KoinModuleScopeRule(config),

                //compose
                NoDirectColorUseRule(config),
                NoCollectAsStateInComposableRule(config),

                //navigation
                NavigationGraphSubgraphRule(config),
                NoExtraParamsInNavigationRule(config),

                //tests
                UseStubsInTestsRule(config),
                RecreateSutRule(config),
                BeforeAnnotationInTest(config)
            )
        )
    }
}

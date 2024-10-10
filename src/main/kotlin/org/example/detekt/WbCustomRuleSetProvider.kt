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
import org.example.detekt.compose.NoCollectAsStateRule
import org.example.detekt.compose.NoDirectColorUseRule
import org.example.detekt.coroutines.FlowSubscriptionInViewModelRule
import org.example.detekt.coroutines.NoManualCoroutineScopeRule
import org.example.detekt.coroutines.StringsMustBeConstantsOrResourceRule
import org.example.detekt.data.RepositoryReturnsResultInWrapperRule
import org.example.detekt.di.KoinModuleHaveScopeRule
import org.example.detekt.di.NoKoinModuleInAppModuleRule
import org.example.detekt.di.NoDependencyInjectionWithByInjectRule
import org.example.detekt.navigation.NavigationGraphsMustBeDividedIntoSubgraphsRule
import org.example.detekt.navigation.NavigationWithSingleArgumentRule
import org.example.detekt.test.BeforeAnnotationInTestRule
import org.example.detekt.test.RecreateSUTForEachTestRule
import org.example.detekt.test.StubsNotMocksInTestsRule

class WbCustomRuleSetProvider : RuleSetProvider {
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
                NoKoinModuleInAppModuleRule(config),
                NoDependencyInjectionWithByInjectRule(config),
                KoinModuleHaveScopeRule(config),

                //compose
                NoDirectColorUseRule(config),
                NoCollectAsStateRule(config),
                StringsMustBeConstantsOrResourceRule(config),

                //navigation
                NavigationGraphsMustBeDividedIntoSubgraphsRule(config),
                NavigationWithSingleArgumentRule(config),

                //tests
                StubsNotMocksInTestsRule(config),
                RecreateSUTForEachTestRule(config),
                BeforeAnnotationInTestRule(config)
            )
        )
    }
}

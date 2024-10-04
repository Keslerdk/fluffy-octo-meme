package org.example.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import org.example.detekt.arch.domain.UseCaseImplDepsRule
import org.example.detekt.arch.domain.UseCaseStructureRule
import org.example.detekt.arch.mappers.LayeredModelsRule
import org.example.detekt.arch.mappers.MapListRule
import org.example.detekt.arch.modules.DependencyRule
import org.example.detekt.arch.modules.ImplLeakRule
import org.example.detekt.arch.modules.TriLayersRule
import org.example.detekt.arch.mvvm.SingleViewStateRule
import org.example.detekt.arch.mvvm.ViewUpdateStateRule
import org.example.detekt.arch.repository.CrudRepositoryNamingRule
import org.example.detekt.arch.repository.NoStateInRepositoryImplRule
import org.example.detekt.arch.repository.SingleSourceRepositoryImplRule
import org.example.detekt.compose.NoCollectAsStateInComposableRule
import org.example.detekt.compose.NoDirectColorUseRule
import org.example.detekt.coroutines.FlowSubscriptionRule
import org.example.detekt.coroutines.ManualCoroutineScopeCreationRule
import org.example.detekt.data.ResultWrapperRule
import org.example.detekt.di.KoinModuleScopeRule
import org.example.detekt.di.KoinModulesDistributionRule
import org.example.detekt.di.NoByInjectRule
import org.example.detekt.navigation.NavigationGraphSubgraphRule
import org.example.detekt.navigation.NoExtraParamsInNavigationRule
import org.example.detekt.test.BeforeAnnotationInTest
import org.example.detekt.test.RecreateSutRule
import org.example.detekt.test.UseStubsInTestsRule

class MyRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = "CustomRules"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                // modules
                TriLayersRule(config),
                DependencyRule(config),
                ImplLeakRule(config),

                //mappers
                LayeredModelsRule(config),
                MapListRule(config),

                //repository
                NoStateInRepositoryImplRule(config),
                CrudRepositoryNamingRule(config),
                SingleSourceRepositoryImplRule(config),
                ResultWrapperRule(config),

                //domain
                UseCaseStructureRule(config),
                UseCaseImplDepsRule(config),

                //mvvm
                ViewUpdateStateRule(config),
                SingleViewStateRule(config),

                //coroutines
                ManualCoroutineScopeCreationRule(config),
                FlowSubscriptionRule(config),

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

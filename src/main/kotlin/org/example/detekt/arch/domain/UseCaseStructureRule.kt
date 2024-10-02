package org.example.detekt.arch.domain

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClass

class UseCaseStructureRule(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION = "UseCase should have only invoke or execute method"
        val availableNames = listOf("execute", "invoke")
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.Style, RULE_DESCRIPTION, Debt.FIVE_MINS)

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        if (klass.name?.contains("UseCase", ignoreCase = true) == true && klass.isInterface()) {
            checkUseCaseStructure(klass)
        }
    }

    private fun checkUseCaseStructure(klass: KtClass) {
        when {
            klass.body?.functions.orEmpty().size > 1 -> report(
                CodeSmell(
                    issue,
                    Entity.Companion.from(klass),
                    "UseCase should have only one function"
                )
            )

            availableNames.none {
                klass.body?.functions?.first()?.name?.contains(it, ignoreCase = true) == true
            } -> report(
                CodeSmell(
                    issue,
                    Entity.Companion.from(klass),
                    "UseCase function should be named 'execute' or 'invoke'"
                )
            )
        }
    }
}

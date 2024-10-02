package org.example.detekt.arch.domain

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClass

class UseCaseImplDepsRule(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION = "UseCase can't have other use case in dependencies"
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.CodeSmell, RULE_DESCRIPTION, Debt.TEN_MINS)

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        if (!klass.isInterface() && klass.name?.contains("UseCase", ignoreCase = true) == true) {
            checkDeps(klass)
        }
    }

    private fun checkDeps(klass: KtClass) {
        val haveUseCaseDeps = klass.primaryConstructor?.valueParameters?.any {
            it.typeReference?.text?.contains("UseCase", ignoreCase = true) == true
        }
        if (haveUseCaseDeps == true) {
            report(CodeSmell(issue, Entity.Companion.from(klass), RULE_DESCRIPTION))
        }
    }
}

package org.example.detekt.arch.domain

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClass

class UseCaseNoRepositoryDependencyRule(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION = "Проверяет, что классы UseCase не зависит от репозиториев."
        const val REPORT_MESSAGE = "UseCase не должен зависеть от репозиториев."
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.CodeSmell, RULE_DESCRIPTION, Debt.TEN_MINS)

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        if (!klass.isInterface() && klass.name?.contains("UseCase", ignoreCase = true) == true) {
            checkRepositoryDeps(klass)
        }
    }

    private fun checkRepositoryDeps(klass: KtClass) {
        val haveUseCaseDeps = klass.primaryConstructor?.valueParameters?.any {
            it.typeReference?.text?.contains("Repository", ignoreCase = true) == true
        }
        if (haveUseCaseDeps == true) {
            report(CodeSmell(issue, Entity.Companion.from(klass), REPORT_MESSAGE))
        }
    }
}

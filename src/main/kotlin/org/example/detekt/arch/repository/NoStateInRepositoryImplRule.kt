package org.example.detekt.arch.repository

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClass

class NoStateInRepositoryImplRule(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION = "Проверяет, что в репозитории не сохраняется состояние."
        const val REPORT_MESSAGE = "Репозитории не должны сохранять состояние"
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.CodeSmell, RULE_DESCRIPTION, Debt.TEN_MINS)

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        if (klass.name?.contains("Repository", ignoreCase = true) == true && !klass.isInterface()) {
            if (klass.getProperties().isNotEmpty()) {
                report(CodeSmell(issue, Entity.Companion.from(klass), REPORT_MESSAGE))
            }
        }
    }
}

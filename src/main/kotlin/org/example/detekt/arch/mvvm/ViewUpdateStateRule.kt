package org.example.detekt.arch.mvvm

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClass

class ViewUpdateStateRule(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION = "View state should only updated with 'update'"
        val forbiddenUpdate = listOf(".value = ", "emit")
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.Warning, RULE_DESCRIPTION, Debt.FIVE_MINS)

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        if (klass.name?.contains("ViewModel", ignoreCase = true) == true) {
            checkStateUpdate(klass)
        }
    }

    private fun checkStateUpdate(klass: KtClass) {
        klass.body?.functions?.forEach { func ->
            val isWrongUpdate = forbiddenUpdate.any { func.text.contains(it) }
            if (isWrongUpdate) {
                report(CodeSmell(issue, Entity.Companion.from(func), RULE_DESCRIPTION))
            }
        }
    }
}

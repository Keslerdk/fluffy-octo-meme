package org.example.detekt.arch.repository

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.getParentOfType

class SingleSourceRepositoryImplRule(config: Config) : Rule(config) {
    private companion object {
        const val DESCRIPTION_RULE = "Data must have one source of truth"
        val availableBodyText = listOf("fromLocal", "fromCache")
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.Warning, DESCRIPTION_RULE, Debt.TEN_MINS)

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        val klass = function.getParentOfType<KtClass>(true)
        if (klass?.name?.contains("Repository", ignoreCase = true) == true && klass.isInterface()) {
            checkCacheInRepository(function)
        }
    }

    private fun checkCacheInRepository(function: KtNamedFunction) {
        val isNotReadFromCache = availableBodyText.none {
            function.text.contains(it, ignoreCase = true)
        }
        if (function.text?.contains("cache", ignoreCase = true) == true && isNotReadFromCache) {
            report(CodeSmell(issue, Entity.Companion.from(function), "read from cache"))
        }
    }
}

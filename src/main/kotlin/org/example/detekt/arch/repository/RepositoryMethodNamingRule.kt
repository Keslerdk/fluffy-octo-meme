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

class RepositoryMethodNamingRule(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION =
            "Проверяет, что публичные методы репозитория имеют правильные имена: 'create', 'get', 'fetch', 'update', или 'delete'."
        val availableNames = listOf("create", "get", "fetch", "update", "delete")
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.CodeSmell, RULE_DESCRIPTION, Debt.FIVE_MINS)

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        val klass = function.getParentOfType<KtClass>(true)
        if (klass?.name?.contains("Repository", ignoreCase = true) == true
            && klass.isInterface()
        ) {
            checkRepositoryFunNames(function)
        }
    }

    private fun checkRepositoryFunNames(function: KtNamedFunction) {
        val isWrongName = availableNames.none { availableName ->
            function.name?.contains(availableName, ignoreCase = true) == true
        }
        if (isWrongName) {
            report(
                CodeSmell(
                    issue,
                    Entity.Companion.from(function),
                    "${availableNames.joinToString()} - должно быть в названии функции"
                )
            )
        }
    }
}

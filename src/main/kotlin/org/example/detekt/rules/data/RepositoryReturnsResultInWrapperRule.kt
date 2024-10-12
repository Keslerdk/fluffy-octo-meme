package org.example.detekt.rules.data

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


class RepositoryReturnsResultInWrapperRule(config: Config) : Rule(config) {

    private companion object {
        val availableWrappers = listOf("Result", "Attemp")

        val RULE_DESCRIPTION =
            "Проверяет, что методы репозитория возвращают данные в оболочке  ${availableWrappers.joinToString()} для явной обработки ошибок и успешных результатов."
        val REPORT_MESSAGE =
            "Данные должны возвращаться в оболочке ${availableWrappers.joinToString()}"
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.Defect, RULE_DESCRIPTION, Debt.TEN_MINS)

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        if (isRepositoryFunction(function) && !hasResultWrapperReturn(function)) {
            report(CodeSmell(issue, Entity.Companion.from(function), REPORT_MESSAGE))
        }
    }

    private fun isRepositoryFunction(function: KtNamedFunction): Boolean {
        val klass = function.getParentOfType<KtClass>(true)
        return klass?.name?.contains("Repository") == true && klass.isInterface()
    }

    private fun hasResultWrapperReturn(function: KtNamedFunction): Boolean {
        val returnType = function.typeReference?.text
        returnType ?: return false
        return availableWrappers.any { wrapperName -> returnType.contains(wrapperName) }
    }

}

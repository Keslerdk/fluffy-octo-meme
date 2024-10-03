package org.example.detekt.data

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

class ResultWrapperRule(config: Config) : Rule(config) {

    private companion object {
        const val RULE_DESCRIPTION =
            "Repository functions should return data in wrapper. Result, Either, Try"
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.Defect, RULE_DESCRIPTION, Debt.TEN_MINS)

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        if (isRepositoryFunction(function) && !hasResultWrapperReturn(function)) {
            report(CodeSmell(issue, Entity.Companion.from(function), RULE_DESCRIPTION))
        }
    }

    private fun isRepositoryFunction(function: KtNamedFunction): Boolean {
        val klass = function.getParentOfType<KtClass>(true)
        return klass?.name?.contains("Repository") == true && klass.isInterface()
    }

    private fun hasResultWrapperReturn(function: KtNamedFunction): Boolean {
        val returnType = function.typeReference?.text
        returnType ?: return false
        return returnType.contains("Result") ||
                returnType.contains("Either") ||
                returnType.contains("Try")
    }

}

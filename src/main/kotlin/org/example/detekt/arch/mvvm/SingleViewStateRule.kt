package org.example.detekt.arch.mvvm

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtCallableDeclaration
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.psiUtil.isPublic

class SingleViewStateRule(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION = "View state should be presentated as one class"
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.Style, RULE_DESCRIPTION, Debt.TEN_MINS)

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        if (klass.name?.contains("ViewModel", ignoreCase = true) == true) {
            checkStateNumber(klass)
        }
    }

    private fun checkStateNumber(klass: KtClass) {
        var numOfState = 0
        checkIsStateReturned(klass.body?.functions.orEmpty()) {
            numOfState++
        }
        checkIsStateReturned(klass.getProperties()) {
            numOfState++
        }

        if (numOfState > 1) {
            report(
                CodeSmell(
                    issue,
                    Entity.Companion.from(klass),
                    "ViewModel should be return only one state"
                )
            )
        }
    }

    private fun checkIsStateReturned(
        list: List<KtCallableDeclaration>,
        ifStateReturned: () -> Unit
    ) {
        list.forEach { ktProperty ->
            val isReturnsState = ktProperty.typeReference?.text?.contains("State")
            if (ktProperty.isPublic && isReturnsState == true) {
                ifStateReturned.invoke()
            }
        }
    }
}

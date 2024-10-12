package org.example.detekt.rules.arch.mvvm

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

class ViewStateClassRule(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION =
            "Проверяет, что для каждого экрана существует общий класс состояния."
        const val REPORT_MESSAGE =
            "Должен быть только один класс состояния для управления состоянием экрана"
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
            report(CodeSmell(issue, Entity.Companion.from(klass), REPORT_MESSAGE))
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

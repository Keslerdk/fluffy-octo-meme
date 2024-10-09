package org.example.detekt.arch.domain

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClass

//todo: change to interactor
class SingleMethodInUseCaseRule(config: Config) : Rule(config) {
    private companion object {
        val availableNames = listOf("execute", "invoke")
        val RULE_DESCRIPTION =
            "Проверяет, что в классе UseCase есть только один метод с именем ${availableNames.joinToString()}."
        const val REPORT_MESSAGE_1 = "В UseCase должен быть только метод"
        val REPORT_MESSAGE_2 = "Метод в UseCase должен называться ${availableNames.joinToString()}"
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.Style, RULE_DESCRIPTION, Debt.FIVE_MINS)

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        if (klass.name?.contains("UseCase", ignoreCase = true) == true && klass.isInterface()) {
            checkUseCaseStructure(klass)
        }
    }

    private fun checkUseCaseStructure(klass: KtClass) {
        when {
            klass.body?.functions.orEmpty().size > 1 -> report(
                CodeSmell(issue, Entity.Companion.from(klass), REPORT_MESSAGE_1)
            )

            availableNames.none {
                klass.body?.functions?.first()?.name?.contains(it, ignoreCase = true) == true
            } -> report(
                CodeSmell(issue, Entity.Companion.from(klass), REPORT_MESSAGE_2)
            )
        }
    }
}

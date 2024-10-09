package org.example.detekt.arch.mappers

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

class ModelMappingRule(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION =
            "Проверяет, что маппингу подвергаются отдельные модели, а не списки моделей."
        const val REPORT_MESSAGE = "Маппинг должен выполняться на уровне отдельных моделей"
    }

    override val issue: Issue
        get() = Issue(
            javaClass.simpleName,
            severity = Severity.Style,
            description = RULE_DESCRIPTION,
            Debt.FIVE_MINS
        )

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        if (function.getParentOfType<KtClass>(true)?.name?.contains("Mapper") == true) {
            function.valueParameters.forEach { param ->
                if (param.typeReference?.text?.contains("List", ignoreCase = true) == true) {
                    report(CodeSmell(issue, Entity.Companion.from(function), REPORT_MESSAGE))
                }
            }
        }
    }
}

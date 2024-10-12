package org.example.detekt.rules.di

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtPropertyDelegate

//todo: в некоторых местаз по другому зависимости не передать
class NoDependencyInjectionWithByInjectRule(config: Config) : Rule(config) {

    private companion object {
        const val DESCRIPTION_RULE = "Проверяет, что зависимости не инъектируются через `by inject`, а предоставляются через конструктор."
        const val REPORT_MESSAGE = "Используйте инъекцию через конструктор"
    }

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Defect,
        description = DESCRIPTION_RULE,
        debt = Debt.FIVE_MINS
    )

    override fun visitPropertyDelegate(delegate: KtPropertyDelegate) {
        super.visitPropertyDelegate(delegate)

        if (delegate.text.contains("by inject", ignoreCase = true)) {
            report(CodeSmell(issue, Entity.from(delegate), REPORT_MESSAGE))
        }
    }
}

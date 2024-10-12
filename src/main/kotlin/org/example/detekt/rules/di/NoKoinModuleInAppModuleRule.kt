package org.example.detekt.rules.di

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtFile

class NoKoinModuleInAppModuleRule(config: Config) : Rule(config) {

    private companion object {
        const val RULE_DESCRIPTION = "Проверяет, что Koin модули не объявлены в app модуле."
        const val REPORT_MESSAGE = "Нет импортов Koin модулей."
    }

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Style,
        description = RULE_DESCRIPTION,
        debt = Debt.FIVE_MINS
    )

    override fun visit(root: KtFile) {
        super.visit(root)
        if (isKoinGraphFile(root)) {
            val haveNoneModuleImports = root.importList?.imports.orEmpty().none {
                it.importPath?.pathStr?.contains("module") == true
            }
            if (!haveNoneModuleImports) {
                report(CodeSmell(issue, Entity.Companion.from(root), REPORT_MESSAGE))
            }
        }
    }

    private fun isKoinGraphFile(root: KtFile) = root.text.contains("startKoin")
}

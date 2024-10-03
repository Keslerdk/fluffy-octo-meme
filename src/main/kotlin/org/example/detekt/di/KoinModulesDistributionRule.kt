package org.example.detekt.di

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtFile

class KoinModulesDistributionRule(config: Config) : Rule(config) {

    private companion object {
        const val RULE_DESCRIPTION =
            "Koin modules should be initialized in each Android-module, not in app"
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
            if (haveNoneModuleImports) {
                report(CodeSmell(issue, Entity.Companion.from(root), "No imports with modules"))
            }
        }
    }

    private fun isKoinGraphFile(root: KtFile) = root.text.contains("startKoin")
}

package org.example.detekt.test

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.*

class UseStubsInTestsRule(config: Config) : Rule(config) {

    private companion object {
        private const val RULE_DESCRIPTION = "Stubs should be used to replace classes in tests, not mocks."
        private const val REPORT_MESSAGE = "Use stubs, not mocks"
        private val mocks = listOf("Mockito", "mockk")
    }

    override val issue = Issue(
        id = "UseStubsInTests",
        severity = Severity.Defect,
        description = RULE_DESCRIPTION,
        debt = Debt.FIVE_MINS
    )

    override fun visit(root: KtFile) {
        super.visit(root)
        val isTestClass = root.name.contains("Test", ignoreCase = true)
        if (isTestClass && haveMocksInImport(root)) {
            report(CodeSmell(issue, Entity.from(root), REPORT_MESSAGE))
        }
    }

    private fun haveMocksInImport(root: KtFile) = root.importList?.imports?.any { import ->
        mocks.any { import.importPath?.pathStr?.contains(it, ignoreCase = true) == true }
    } == true
}

package org.example.detekt.test

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.*

class StubsNotMocksInTestsRule(config: Config) : Rule(config) {

    private companion object {
        private const val RULE_DESCRIPTION =
            "Проверяет, что в тестах используются стабы, а не моки в тестах."
        private const val REPORT_MESSAGE = "Заменить моки на стабы."
        private val mocks = listOf("Mockito", "mockk")
    }

    override val issue = Issue(
        id = javaClass.simpleName,
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

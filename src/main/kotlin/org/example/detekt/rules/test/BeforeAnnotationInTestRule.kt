package org.example.detekt.rules.test

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.rules.hasAnnotation
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.forEachDescendantOfType

class BeforeAnnotationInTestRule(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION =
            "Проверяет, что в каждом тесте присутствует аннотация @Before или @BeforeEach."
        const val REPORT_MESSAGE =
            "Используйте setup метод для инициализации переменных перед тестом."
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.CodeSmell, RULE_DESCRIPTION, Debt.FIVE_MINS)

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        val isTestClass = klass.name?.contains("Test", ignoreCase = true) ?: return
        if (isTestClass && !isContainsSetUpMethod(klass)) {
            report(CodeSmell(issue, Entity.Companion.from(klass), REPORT_MESSAGE))
        }
    }


    private fun isContainsSetUpMethod(klass: KtClass): Boolean {
        var haveBeforeAnnotation = false
        klass.forEachDescendantOfType<KtNamedFunction> { func ->
            haveBeforeAnnotation =
                haveBeforeAnnotation || func.hasAnnotation("Before")
                        || func.hasAnnotation("BeforeEach")
        }
        return haveBeforeAnnotation
    }
}

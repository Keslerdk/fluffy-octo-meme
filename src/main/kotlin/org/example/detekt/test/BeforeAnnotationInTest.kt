package org.example.detekt.test

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

class BeforeAnnotationInTest(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION = "Every test should have setup method with @Before annotation"
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.CodeSmell, RULE_DESCRIPTION, Debt.FIVE_MINS)

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        val isTestClass = klass.name?.contains("Test", ignoreCase = true) ?: return
        if (isTestClass && !isContainsSetUpMethod(klass)) {
            report(CodeSmell(issue, Entity.Companion.from(klass), RULE_DESCRIPTION))
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

package org.example.detekt.test

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtClass

class RecreateSutRule(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION = "Sut must recreate for each test"
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.Warning, RULE_DESCRIPTION, Debt.FIVE_MINS)

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        val isClassTest = klass.name?.contains("Test", ignoreCase = true) ?: return
        val haveSutProperty = klass.getProperties().any { property ->
            property.name == "sut" && !property.hasModifier(KtTokens.LATEINIT_KEYWORD)
        }
        if (isClassTest && !haveSutProperty) {
            report(CodeSmell(issue, Entity.Companion.from(klass), "No lateinit sut property in test class"))
        }
    }
}

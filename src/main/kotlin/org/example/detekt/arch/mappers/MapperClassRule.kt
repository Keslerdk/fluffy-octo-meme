package org.example.detekt.arch.mappers

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.example.detekt.arch.getCurrentLayer
import org.jetbrains.kotlin.psi.KtClass

class MapperClassRule(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION = "This rule reposts if there is no mapper classes"
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.Warning, RULE_DESCRIPTION, Debt.TEN_MINS)

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        val type = getCurrentLayer(klass.containingFile.name)
    }

}

package org.example.detekt.di

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtPropertyDelegate

class NoByInjectRule(config: Config) : Rule(config) {

    private companion object {
        const val DESCRIPTION_RULE = "No dependencies can't added by inject."
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
            report(
                CodeSmell(
                    issue,
                    Entity.from(delegate),
                    "'by inject' is not allowed. Add deps in constructor"
                )
            )
        }
    }
}

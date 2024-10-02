package org.example.detekt.arch.domain

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity

class UseCaseNaming(config: Config) : Rule(config) {
    companion object {
        const val RULE_DESCRIPTION = "UseCase name should start with verb"
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.Style, RULE_DESCRIPTION, Debt.FIVE_MINS)
}

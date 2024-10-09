package org.example.detekt.arch.modules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.rules.isInternal
import org.example.detekt.arch.ModuleType
import org.example.detekt.getCurrentLayer
import org.example.detekt.isCommon
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.getParentOfType
import org.jetbrains.kotlin.psi.psiUtil.isPrivate

class ImplementationIsolationRule(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION =
            "Проверяет, что в слоях 'data' и 'ui' все внутренние реализации помечены как internal"
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.Defect, RULE_DESCRIPTION, Debt.FIVE_MINS)

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)

        if (isCommon(klass.containingFile.name)) return

        when (val type = getCurrentLayer(klass.containingFile.name)) {
            ModuleType.DATA, ModuleType.UI -> checkClass(klass, type)
            ModuleType.DOMAIN -> checkDomainClass(klass)
            else -> {}
        }
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        val type = getCurrentLayer(function.containingFile.name) ?: return
        if (!function.isInternal() && shouldBeInternal(function)) {
            report(CodeSmell(issue, Entity.Companion.from(function), getReportMessage(type)))
        }
    }

    private fun checkClass(ktClass: KtClass, type: ModuleType) {
        if (!ktClass.isInternal()) {
            report(CodeSmell(issue, Entity.Companion.from(ktClass), getReportMessage(type)))
        }
    }

    private fun checkDomainClass(ktClass: KtClass) {
        if (!ktClass.isInternal() && !ktClass.isInterface() && !ktClass.isData()) {
            report(
                CodeSmell(
                    issue,
                    Entity.Companion.from(ktClass),
                    getReportMessage(ModuleType.DOMAIN)
                )
            )
        }
    }

    private fun shouldBeInternal(function: KtNamedFunction): Boolean {
        val isSeparated = function.getParentOfType<KtClass>(true) == null
        return function.name?.contains("module", ignoreCase = true) == false
                && function.name?.contains("route", ignoreCase = true) == false
                && isSeparated && !function.isPrivate() && !isCommon(function.containingFile.name)
    }


    private fun getReportMessage(type: ModuleType) =
        "Все внутренние реализации ${type.possibleNames.first()} должны быть помечены internal"
}

package org.example.detekt.arch.mappers

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.rules.hasAnnotation
import org.example.detekt.arch.ModuleType
import org.example.detekt.getCurrentLayer
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.getParentOfType

//todo: mutex
private val dataClasses: MutableSet<KtClass> = mutableSetOf()

class SeparateDataModelPerLayerRule(config: Config) : Rule(config) {
    private companion object {
        const val RULE_DESCRIPTION =
            "Проверяет, что каждый слой имеет свою собственную модель данных и они не используются в других слоях."
        const val REPORT_MESSAGE_DATA = "Domain модель не должна использоваться для получения данных"
        const val REPORT_MESSAGE_DOMAIN = "Domain модель не должна использоваться для отображения данных"
    }

    override val issue: Issue
        get() = Issue(javaClass.simpleName, Severity.Defect, RULE_DESCRIPTION, Debt.TEN_MINS)

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)

        val type = getCurrentLayer(klass.containingFile.name)
        if (!klass.isData() || type != ModuleType.DOMAIN) return
        dataClasses.add(klass)

        //todo: как проверить что не используется в дата-слое, если используется не serialization?
        if (klass.hasAnnotation("Serializable")) {
            report(CodeSmell(issue, Entity.Companion.from(klass), REPORT_MESSAGE_DATA))
        }
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        if (function.hasAnnotation("Composable")) {
            val hasDomainImport =
                function.getParentOfType<KtFile>(true)?.importList?.imports?.any { import ->
                    val importPath = import.importPath?.pathStr ?: return@any false
                    dataClasses.any { klass ->
                        val packageName = klass.fqName?.parent()?.asString() ?: return@any false
                        val className = klass.name ?: return@any false
                        importPath.endsWith(".$className") && importPath.startsWith(packageName)
                    }
                }

            if (hasDomainImport == true) {
                report(CodeSmell(issue, Entity.Companion.from(function), REPORT_MESSAGE_DOMAIN))
            }
        }
    }


    /**
    private class MyVisitor : DetektVisitor() {
    val dataClasses: MutableSet<KtClass> = mutableSetOf()
    val funParamTypes: MutableSet<String> = mutableSetOf()

    fun getDomainModels(): List<KtClass> {
    return dataClasses.filter { getCurrentLayer(it.name.orEmpty()) == ModuleType.DOMAIN }
    .filter { funParamTypes.contains(it.name) }
    }

    override fun visitClass(klass: KtClass) {
    super.visitClass(klass)
    if (klass.isData()) {
    dataClasses.add(klass)
    }
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
    super.visitNamedFunction(function)
    if (function.hasAnnotation("Composable")) {
    function.valueParameters.forEach { param ->
    param.typeReference?.name?.let { funParamTypes.add(it) }
    }
    }
    }
    }
     **/
}

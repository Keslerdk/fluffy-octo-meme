package org.example.detekt.report

import io.gitlab.arturbosch.detekt.api.Detektion
import io.gitlab.arturbosch.detekt.api.OutputReport
import kotlinx.html.*
import kotlinx.html.stream.createHTML

class HtmlReport : OutputReport() {

    override val ending: String = "html"
    override val name: String = "Custom HTML Issue Report"

    override fun render(detektion: Detektion): String? {
        val findings = detektion.findings.flatMap { it.value }
        if (findings.isEmpty()) return null

        // Построение HTML-содержимого с таблицей
        val htmlContent = createHTML().html {
            head {
                title("Detekt Issue Report")
                style {
                    +"""
                        table {
                            border-collapse: collapse;
                            width: 100%;
                        }
                        th, td {
                            border: 1px solid black;
                            padding: 8px;
                            text-align: left;
                        }
                    """.trimIndent()
                }
            }
            body {
                h1 { +"Detekt Issue Report" }
                table {
                    tr {
                        th { +"RuleSet" }
                        th { +"Правило" }
                        th { +"Количество ошибок" }
                        th { +"Описание" }
                    }

                    // Проход по findings, группировка по наборам правил
                    detektion.findings
                        .filter { it.value.isNotEmpty() }
                        .forEach { (group, groupFindings) ->
                            val groupIssueNumber = groupFindings.size
                            tr {
                                td { +group }
                                td { } // Пустая ячейка для правила
                                td { +groupIssueNumber.toString() }
                                td { } // Пустая ячейка для описания
                            }

                            // Отображение конкретных правил в группе
                            val groupIssues = groupFindings.groupBy { it.id }
                            groupIssues.forEach { (ruleId, findingList) ->
                                val count = findingList.size
                                val description = findingList.firstOrNull()?.issue?.description.orEmpty()
                                tr {
                                    td { } // Пустая ячейка для RuleSet
                                    td { +ruleId }
                                    td { +count.toString() }
                                    td { +description }
                                }
                            }
                        }
                }
            }
        }

        return htmlContent
    }
}

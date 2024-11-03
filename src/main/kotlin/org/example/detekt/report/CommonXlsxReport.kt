package org.example.detekt.report

import io.gitlab.arturbosch.detekt.api.Detektion
import io.gitlab.arturbosch.detekt.api.Finding
import io.gitlab.arturbosch.detekt.api.OutputReport
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class CommonXlsxReport : OutputReport() {

    private companion object {
        const val COMMON_REPORT_FILE_PATH = "/Users/kesler/Downloads/detekt_report_common2.xlsx"
        const val DETEKT_CONFIG_PATH =
            "/Users/kesler/StudioProjects/staga/fluffy-octo-meme/config/detekt.yml"
        const val STUDENTS_PROJECTS_FOLDER_NAME = "staga"
    }

    override val ending: String = "xlsx"

    override fun render(detektion: Detektion): String? {
        // Путь к файлу отчета
        val reportFile = File(COMMON_REPORT_FILE_PATH)

        // Открытие существующего или создание нового Excel файла
        val workbook = XSSFWorkbook()

        val sheet = workbook.getSheet("Detekt Issues") ?: workbook.createSheet("Detekt Issues")
        val yamlContent = File(DETEKT_CONFIG_PATH).readText()
        val activeRules = getActiveRules(yamlContent)
        createTitle(sheet, activeRules)

        detektion.findings
            .flatMap { it.value }
            .groupBy { finding ->
                val pathItems = finding.file.split('/')
                val folderIndex = pathItems.indexOfFirst { it == STUDENTS_PROJECTS_FOLDER_NAME }
                pathItems[folderIndex + 1]
            }.onEach { (name, findings) ->
                createStudentRow(sheet, name, findings)
            }

        FileOutputStream(reportFile).use { workbook.write(it) }
        workbook.close()

        return reportFile.absolutePath
    }

    private fun createTitle(sheet: Sheet, rules: Map<String, List<String>>) {
        val ruleSetRow = sheet.createRow(0)
        val ruleRow = sheet.createRow(1)

        val list = rules
            .flatMap { entry ->
                entry.value.map { entry.key to it }.toMutableList()
                    .apply { add(entry.key to "Sum") }.toList()
            }

        list.forEachIndexed { index, (ruleSet, rule) ->
            ruleSetRow.createCell(index + 1).setCellValue(ruleSet)
            ruleRow.createCell(index + 1).setCellValue(rule)
        }
    }

    private fun createStudentRow(sheet: Sheet, name: String, findings: List<Finding>) {
        val studentRow = sheet.createRow(sheet.physicalNumberOfRows)
        studentRow.createCell(0).setCellValue(name)
        findings.groupBy { it.id }
            .mapValues { it.value.size }
            .forEach { (rule, count) ->
                val columnIndex = sheet.getRow(1).cellIterator().asSequence()
                    .find { it.stringCellValue == rule }?.columnIndex
                columnIndex?.let { studentRow.createCell(it).setCellValue(count.toString()) }
            }
    }
}

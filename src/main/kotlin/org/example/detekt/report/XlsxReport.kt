package org.example.detekt.report

import io.gitlab.arturbosch.detekt.api.Detektion
import io.gitlab.arturbosch.detekt.api.OutputReport
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class XlsxReport : OutputReport() {

    private companion object {
        const val COMMON_REPORT_FILE_PATH = "/Users/kesler/Downloads/detekt_report_common.xlsx"
        const val DETEKT_CONFIG_PATH =
            "/Users/kesler/StudioProjects/staga/fluffy-octo-meme/config/detekt.yml"
        const val STUDENTS_PROJECTS_FOLDER_NAME = "staga"
    }

    override val ending: String = "xlsx"
    override val name: String = "Custom XLSX Issue Report"

    override fun render(detektion: Detektion): String? {
        val findings = detektion.findings.flatMap { it.value }
        if (findings.isEmpty()) return null

        // Путь к файлу отчета
        val reportFile = File(COMMON_REPORT_FILE_PATH)

        // Открытие существующего или создание нового Excel файла
        val workbook = if (reportFile.exists()) {
            WorkbookFactory.create(reportFile.inputStream())
        } else {
            XSSFWorkbook()
        }
        val sheet = workbook.getSheet("Detekt Issues") ?: workbook.createSheet("Detekt Issues")

        // Если это первый запуск, добавляем двойной заголовок
        if (sheet.physicalNumberOfRows == 0) {
            val yamlContent = File(DETEKT_CONFIG_PATH).readText()
            val activeRules = getActiveRules(yamlContent)
            createTitle(sheet, activeRules)
            // Словарь для хранения столбцов по ruleSetId и ruleId
        }

        // Определение строки для текущего проекта
        val name = detektion.findings.values.flatten().firstOrNull()?.file?.let { path ->
            val list = path.split("/")
            val index = list.indexOfFirst { it == STUDENTS_PROJECTS_FOLDER_NAME } + 1
            list[index]
        } ?: "Unknown"

        //перезаписываем существующую строку
//        val rowNum = (0..<sheet.physicalNumberOfRows).find {
//            sheet.getRow(it).getCell(0).stringCellValue == name
//        } ?: sheet.physicalNumberOfRows
        val projectRow = sheet.createRow(sheet.physicalNumberOfRows)
        projectRow.createCell(0).setCellValue(name) // Путь к проекту

        // Подсчет ошибок для каждого ruleId
        detektion.findings
            .forEach { (_, groupFindings) ->
                groupFindings
                    .groupBy { it.id }
                    .forEach { (_, ruleFindings) ->
                        ruleFindings.forEach { rule ->
                            val columnIndex = sheet.getRow(1).cellIterator().asSequence()
                                .find { it.stringCellValue == rule.id }?.columnIndex
                            columnIndex?.let {
                                projectRow.createCell(it).setCellValue(ruleFindings.size.toDouble())
                            }
                        }
                    }
            }

        // Сохранение файла
        FileOutputStream(reportFile).use { workbook.write(it) }
        workbook.close()

        return reportFile.absolutePath
    }

    private fun createTitle(sheet: Sheet, rules: Map<String, List<String>>) {
        val ruleSetRow = sheet.createRow(0)
        val ruleRow = sheet.createRow(1)

        val list = rules.flatMap { entry ->
            entry.value.map { entry.key to it }
        }.distinctBy { it.second }

        list.forEachIndexed { index, (ruleSet, rule) ->
            ruleSetRow.createCell(index + 1).setCellValue(ruleSet)
            ruleRow.createCell(index + 1).setCellValue(rule)
        }
    }
}

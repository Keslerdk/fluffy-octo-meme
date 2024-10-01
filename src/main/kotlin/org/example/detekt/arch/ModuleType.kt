package org.example.detekt.arch

enum class ModuleType(val possibleNames: List<String>) {
    DATA(listOf("data", "repository")),
    DOMAIN(listOf("domain")),
    UI(listOf("ui", "presentation")),
}

# detekt custom rule template

This repository is a template. You can use it to generate your own repository to write and share your custom rules.

## How to use it

add to root gradle 
```kotlin

plugins {
    ...
    alias(libs.plugins.detekt) apply true
}

dependencies {
    detektPlugins(libs.detekt.formatting)
    detektPlugins(project(":rules"))
    detektPlugins ("io.nlopez.compose.rules:detekt:0.4.15")
    detektPlugins("ru.kode:detekt-rules-compose:1.4.0")
}

val projectSource = file(projectDir)
val configFile = files("$projectDir/config/detekt/detekt.yml")
val baselineFile = file("$projectDir/config/detekt/baseline.xml")
val kotlinFiles = "**/*.kt"
val gradleFiles = "**/*.kts"
val resourceFiles = "**/resources/**"
val buildFiles = "**/build/**"
val testFiles = "**/src/test/**"
val ruleModule = "**/rules/**"

tasks.register<Detekt>("detektAll") {
    dependsOn(":rules:assemble")

    description = "Custom DETEKT build for all modules"
    ignoreFailures = false
    parallel = true
    setSource(projectSource)
    allRules = false
    config.setFrom(file("$projectDir/config/detekt/detekt.yml"))
    include(kotlinFiles, gradleFiles)
    exclude(resourceFiles, testFiles, ruleModule)
    reports {
        html.required.set(true)
    }
}
```

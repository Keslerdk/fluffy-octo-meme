plugins {
    kotlin("jvm") version "2.0.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

java {
    sourceCompatibility = JavaVersion.VERSION_19
    targetCompatibility = JavaVersion.VERSION_19
}


dependencies {
    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.23.7")

    implementation("org.jetbrains.kotlinx:kotlinx-html:0.11.0") {
        exclude(group = "org.jetbrains.kotlin")
    }
    implementation("org.apache.poi:poi-ooxml:5.2.3")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")// Версия может быть новее
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.1")
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "19"
    }
}

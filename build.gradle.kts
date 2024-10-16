import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter

plugins {
    kotlin("jvm") version "2.0.10"
//    `maven-publish`
}

//group = "org.example.detekt"
//version = "1.0-SNAPSHOT"

dependencies {
    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.23.7")

    implementation("org.jetbrains.kotlinx:kotlinx-html:0.11.0") {
        exclude(group = "org.jetbrains.kotlin")
    }

//    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:1.23.7")
//    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
//    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
}

//kotlin {
//    jvmToolchain(22)
//}

//tasks.withType<Test>().configureEach {
//    useJUnitPlatform()
//    systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")
//    systemProperty("compile-snippet-tests", project.hasProperty("compile-test-snippets"))
//}
//
//publishing {
//    publications {
//        create<MavenPublication>("mavenJava") {
//            from(components["java"])
//        }
//    }
//}

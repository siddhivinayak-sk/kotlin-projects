//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.22"
    application
    idea
    eclipse
    java
}

group = "com.sk.kotlin"
version = "1.0-SNAPSHOT"

val mockkVersion: String by project
val kluentVersion: String by project

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("io.projectreactor:reactor-core:3.4.19")
    testImplementation("io.projectreactor:reactor-test:3.4.19")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testImplementation("org.amshove.kluent:kluent:$kluentVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

//tasks.withType<KotlinCompile> {
//    kotlinOptions.jvmTarget = "1.8"
//}

application {
    mainClass.set("MainKt")
}


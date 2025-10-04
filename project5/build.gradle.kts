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
val bcVersion: String by project

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation("io.projectreactor:reactor-core:3.6.5")
    implementation("com.nimbusds:nimbus-jose-jwt:9.25.6")
    implementation("org.yaml:snakeyaml:2.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("io.micrometer:context-propagation:latest.integration")
    implementation("org.apache.tika:tika-core:2.9.2")

    implementation("org.bouncycastle:bcprov-jdk18on:$bcVersion")
    implementation("org.bouncycastle:bcpkix-jdk18on:$bcVersion")
    implementation("org.bouncycastle:bcutil-jdk18on:$bcVersion")
    implementation("org.bouncycastle:bcpg-jdk18on:$bcVersion")

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


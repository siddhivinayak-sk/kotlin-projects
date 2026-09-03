plugins {
    id("org.jetbrains.kotlin.jvm") version "2.4.10"
    application
}

group = "com.sk"
version = "0.0.1-SNAPSHOT"
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.guava:guava:33.7.1-jre")
    implementation("com.google.auto.service:auto-service:1.1.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.14.4")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.14.4")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.6.0")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useKotlinTest()
        }
    }
}

application {
    mainClass.set("com.sk.project8.AppKt")
}

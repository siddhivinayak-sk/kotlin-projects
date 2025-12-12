import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.3.0-RC"
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
val passayVersion: String by project
val nbvcxzVersion: String by project
val pdfboxVersion: String by project
val poiVersion: String by project
val flexJsonVersion: String by project
val jnativehookVersion: String by project
val gsonVersion: String by project
val jasperreportsVersion: String by project
val derbyclientVersion: String by project
val jaxbapiVersion: String by project
val twelvemonkeysVersion: String by project
val jaiimageiocoreVersion: String by project
val image4jVersion: String by project
val imgscalrlibVersion: String by project
val azureidentityVersion: String by project
val azuresecuritykeyvaultsecretsVersion: String by project
val azurekeyvaultVersion: String by project
val msadal4jVersion: String by project
val jmhVersion: String by project
val jolcoreVersion: String by project
val minioVersion: String by project
val httpclientVersion: String by project
val jakartavalidationapiVersion: String by project
val awssdkbomVersion: String by project
val apacheLog4jCoreVersion: String by project
val javaxServletApiVersion: String by project

repositories {
    mavenCentral()
    //jcenter()
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
    implementation("me.gosimple:nbvcxz:${nbvcxzVersion}")
    implementation("org.passay:passay:${passayVersion}")
    implementation("org.apache.pdfbox:pdfbox:${pdfboxVersion}")
    implementation("org.apache.poi:poi:${poiVersion}")
    implementation("org.apache.poi:poi-ooxml:${poiVersion}")
    implementation("org.apache.poi:poi-scratchpad:${poiVersion}")
    implementation("org.apache.poi:poi-ooxml-full:${poiVersion}")
    implementation("org.apache.poi:poi-excelant:${poiVersion}")
    implementation("org.apache.poi:poi-examples:${poiVersion}")
    implementation("net.sf.flexjson:flexjson:${flexJsonVersion}")
    implementation("com.1stleg:jnativehook:${jnativehookVersion}")
    implementation("com.google.code.gson:gson:${gsonVersion}")
    implementation("com.1stleg:jnativehook:${jnativehookVersion}")
    implementation("com.google.code.gson:gson:${gsonVersion}")
    implementation("net.sf.jasperreports:jasperreports:${jasperreportsVersion}")
    implementation("org.apache.derby:derbyclient:${derbyclientVersion}")
    implementation("javax.xml.bind:jaxb-api:${jaxbapiVersion}")
    implementation("com.twelvemonkeys.imageio:imageio-core:${twelvemonkeysVersion}")
    //implementation("com.twelvemonkeys.imageio:common-image:${twelvemonkeysVersion}")
    implementation("com.twelvemonkeys.imageio:imageio-metadata:${twelvemonkeysVersion}")
    implementation("com.twelvemonkeys.imageio:imageio-tiff:${twelvemonkeysVersion}")
    implementation("com.twelvemonkeys.imageio:imageio-jpeg:${twelvemonkeysVersion}")
    implementation("com.github.jai-imageio:jai-imageio-core:${jaiimageiocoreVersion}")
    implementation("org.jclarion:image4j:${image4jVersion}")
    implementation("org.imgscalr:imgscalr-lib:${imgscalrlibVersion}")
    implementation("com.azure:azure-identity:${azureidentityVersion}")
    implementation("com.azure:azure-security-keyvault-secrets:${azuresecuritykeyvaultsecretsVersion}")
    implementation("com.microsoft.azure:azure-keyvault:${azurekeyvaultVersion}")
    implementation("com.microsoft.azure:adal4j:${msadal4jVersion}")
    implementation("org.openjdk.jmh:jmh-core:${jmhVersion}")
    implementation("org.openjdk.jmh:jmh-generator-annprocess:${jmhVersion}")
    implementation("org.openjdk.jol:jol-core:${jolcoreVersion}")
    implementation("io.minio:minio:${minioVersion}")
    implementation("org.apache.httpcomponents:httpclient:${httpclientVersion}")
    implementation("jakarta.validation:jakarta.validation-api:${jakartavalidationapiVersion}")
    implementation("software.amazon.awssdk:bom:${awssdkbomVersion}")
    implementation("software.amazon.awssdk:s3:${awssdkbomVersion}")
    implementation("org.apache.logging.log4j:log4j-core:${apacheLog4jCoreVersion}")
    implementation("javax.servlet:servlet-api:${javaxServletApiVersion}")

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
//    compilerOptions {
//        jvmTarget.set(JvmTarget.JVM_25)
//    }
//}

application {
    mainClass.set("com.sk.ktl.nimbus.TokenToolKt")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--enable-preview")
    options.compilerArgs.add("-Xlint:deprecation")
    options.compilerArgs.add("-Xlint:preview")
    options.release.set(25)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_25)
    }
}

tasks.withType<JavaExec> {
    jvmArgs("--enable-preview")
}

tasks.withType<Test> {
    jvmArgs("--enable-preview")
}

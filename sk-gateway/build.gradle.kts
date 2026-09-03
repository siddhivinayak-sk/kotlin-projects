import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.springframework.boot") version "3.5.16"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "2.4.10"
    kotlin("plugin.spring") version "2.4.10"
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

extra["springCloudVersion"] = "2025.0.3"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.springframework.cloud:spring-cloud-starter-gateway")
	implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")
	//implementation("org.springframework.cloud:spring-cloud-starter-netflix-hystrix")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
	compilerOptions {
		freeCompilerArgs.add("-Xjsr305=strict")
		jvmTarget.set(JvmTarget.JVM_25)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

//tasks.register<DocumentTask>("create-readme") {
//    val documents = listOf<String>("introduction", "details")
//    sourceDocuments.set(documents)
//}
//
//open class DocumentTask @Inject constructor(objectFactory: ObjectFactory) : DefaultTask() {
//    @get:Input val sourceDocuments: ListProperty<String> = objectFactory.listProperty(String::class.java)
//
//    @TaskAction
//    fun readmeMerge() {
//        val readme = File("README.md")
//        readme.deleteOnExit()
//        readme.createNewFile()
//        for(fileName in sourceDocuments.get()) {
//            readme.appendText(File("${project.projectDir}/documents/$fileName.md").readText())
//        }
//    }
//}

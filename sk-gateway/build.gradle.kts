//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
}

group = "com.sk"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2024.0.0"

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

//tasks.withType<KotlinCompile> {
//	kotlinOptions {
//		freeCompilerArgs = listOf("-Xjsr305=strict")
//		jvmTarget = "1.8"
//	}
//}
//
//tasks.withType<Test> {
//	useJUnitPlatform()
//}
//
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

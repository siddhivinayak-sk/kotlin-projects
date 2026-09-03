import org.asciidoctor.gradle.jvm.AsciidoctorTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	id("org.springframework.boot") version "3.5.16"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.asciidoctor.jvm.convert") version "4.0.5"
	id("org.springframework.cloud.contract") version "4.3.4"
	eclipse
	`maven-publish`
	idea
	kotlin("jvm") version "2.4.10"
	kotlin("plugin.spring") version "2.4.10"
	kotlin("plugin.jpa") version "2.4.10"
}

group = "com.sk"
version = "0.0.1-SNAPSHOT"
java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenLocal()
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	//implementation("org.springframework.boot:spring-boot-starter-redis-reactive")
	//implementation("org.springframework.boot:spring-boot-starter-web")
	//implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.data:spring-data-rest-hal-explorer")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	//implementation("org.springframework.security:spring-security-core")
	//implementation("org.springframework.security:spring-security-config")
	//implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.restdocs:spring-restdocs-webtestclient")
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.9.0")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation(kotlin("test"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.junit.jupiter:junit-jupiter-params")
	testImplementation("org.amshove.kluent:kluent:1.73")
	testImplementation("io.mockk:mockk:1.14.11")
	testImplementation("io.rest-assured:spring-web-test-client:6.0.1")
	//testImplementation("io.rest-assured:spring-mock-mvc:5.1.0")
	testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock:4.3.4")
	testImplementation("org.springframework.cloud:spring-cloud-contract-verifier:4.3.4")
	//implementation("org.springframework.restdocs:spring-restdocs")
	//implementation("org.springframework.restdocs:spring-restdocs-core")
	//testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo") //Enable it for embeded mongo db
	//testImplementation("org.springframework.boot:spring-boot-starter-data-mongodb")
}

tasks.withType<KotlinCompile> {
	compilerOptions {
		freeCompilerArgs.set(listOf("-Xjsr305=strict"))
		jvmTarget.set(JvmTarget.JVM_25)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
}


/**
 * To use asciidoctor, first run: gradle clean build
 * then run: gradle asciidoctor
 */
tasks.withType<AsciidoctorTask> {
	sourceDir(file("src/main/asciidoc"))
	attributes(mapOf("snippets" to "build/generated-snippets"))
}

contracts {
	setTestFramework("JUNIT5")
	setTestMode("WEBTESTCLIENT")
	setBasePackageForTests("com.sk.project6")
	setBaseClassForTests("com.sk.project6.Project6ApplicationTests")
	//setPackageWithBaseClasses("com.sk.project4.resource")
	//setBaseClassMappings(HashMap(Map.of(".*intoxication.*", "com.example.intoxication.BeerIntoxicationBase")))
}


tasks.withType<GenerateModuleMetadata> {
	enabled = true
	suppressedValidationErrors.add("enforced-platform")
}

java {
	withJavadocJar()
	withSourcesJar()
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			from(components["java"])
			artifact(tasks["verifierStubsJar"])
			suppressPomMetadataWarningsFor("runtimeElements")
			withBuildIdentifier()
		}
	}
	repositories {
		mavenLocal()
	}
}
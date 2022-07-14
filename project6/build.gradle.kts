import org.asciidoctor.gradle.jvm.AsciidoctorTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.incremental.withSubtypes
import java.util.HashMap
import java.util.Map

plugins {
	id("org.springframework.boot") version "2.6.8"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
	id("org.springframework.cloud.contract") version "3.1.3"
	eclipse
	`maven-publish`
	idea
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
}

group = "com.sk"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenLocal()
	mavenCentral()
	jcenter()
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
	implementation("org.springframework.restdocs:spring-restdocs-webtestclient:2.0.6.RELEASE")
	implementation("org.springdoc:springdoc-openapi-webflux-ui:1.6.9")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation(kotlin("test"))
	testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
	testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
	testImplementation("org.amshove.kluent:kluent:1.68")
	testImplementation("io.mockk:mockk:1.12.4")
	testImplementation("io.rest-assured:spring-web-test-client:5.1.0")
	//testImplementation("io.rest-assured:spring-mock-mvc:5.1.0")
	testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock:3.1.3")
	testImplementation("org.springframework.cloud:spring-cloud-contract-verifier:3.1.3")
	//implementation("org.springframework.restdocs:spring-restdocs")
	//implementation("org.springframework.restdocs:spring-restdocs-core")
	//testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo") //Enable it for embeded mongo db
	//testImplementation("org.springframework.boot:spring-boot-starter-data-mongodb")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
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
	setSourceDir(file("src/main/asciidoc"))
	attributes(HashMap(Map.of("snippets", "build/generated-snippets")))
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
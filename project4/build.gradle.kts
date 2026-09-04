import org.asciidoctor.gradle.jvm.AsciidoctorTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	id("org.springframework.boot") version "3.5.16"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.asciidoctor.jvm.convert") version "4.0.5"
	id("org.springframework.cloud.contract") version "4.3.4"
	id("org.springdoc.openapi-gradle-plugin") version "1.9.0"
	jacoco
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

	maven { setUrl("https://repo.spring.io/snapshot") }
	maven { setUrl("https://repo.spring.io/milestone") }
	maven { setUrl("https://repo.spring.io/release") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	//implementation("org.springframework.boot:spring-boot-starter-redis-reactive")
	//implementation("org.springframework.boot:spring-boot-starter-web")
	//implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.data:spring-data-rest-hal-explorer")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	//implementation("org.springframework.boot:spring-boot-starter-data-mongodb") //Non-reative mongo data
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.security:spring-security-core")
	implementation("org.springframework.security:spring-security-config")
	implementation("org.springframework.boot:spring-boot-starter-security")
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
	//testImplementation("io.rest-assured:spring-mock-mvc:5.1.0") //Used with Mock MVC
	testImplementation("org.springframework.cloud:spring-cloud-contract-verifier:4.3.4")
	testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock:4.3.4")
	testImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner:4.3.4")
	//implementation("org.springframework.restdocs:spring-restdocs")  //For manual restdocs configuration
	//implementation("org.springframework.restdocs:spring-restdocs-core") //For manual restdocs configuration
	//testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo") //Enable it for embeded mongo db
	testImplementation("com.sk:project6:0.0.1-SNAPSHOT")
	testImplementation("com.sk:project6:0.0.1-SNAPSHOT:stubs")

	//For test container for MongoDB
	testImplementation("org.testcontainers:testcontainers:1.21.4")
	testImplementation("org.testcontainers:junit-jupiter:1.21.4")
	testImplementation("org.testcontainers:mongodb:1.21.4")

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
	finalizedBy(tasks.jacocoTestReport)
}

tasks.withType<JacocoReport> {
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
		csv.required.set(true)
		html.outputLocation.set(layout.buildDirectory.dir("$buildDir/reports/jacoco"))
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
	setBasePackageForTests("com.sk.project4")
	setBaseClassForTests("com.sk.project4.Project4ApplicationTests")
	//setPackageWithBaseClasses("com.sk.project4.resource")
	//setBaseClassMappings(HashMap(Map.of(".*intoxication.*", "com.example.intoxication.BeerIntoxicationBase")))
	setConvertToYaml(true) //True will generate stub in YAML format but custom function will not work in this case
}

openApi {
	apiDocsUrl.set("http://localhost:8080/v3/api-docs")
	outputDir.set(file("$buildDir/docs"))
	outputFileName.set("openapi-spec.json")
	waitTimeInSeconds.set(30)
	//forkProperties.set("-Dspring.profiles.active=special")
	//groupedApiMappings.set(["https://localhost:8080/v3/api-docs/groupA" to "swagger-groupA.json", "https://localhost:8080/v3/api-docs/groupB" to "swagger-groupB.json"])
}

jacoco {
	toolVersion = "0.8.15"
	reportsDirectory.set(layout.buildDirectory.dir("$buildDir/reports/jacoco"))
}


/**
 * Spring Rest Docs and Groovy Contract Documentation
 * https://github.com/spring-cloud-samples/spring-cloud-contract-samples/blob/main/docs/tutorials/rest_docs.adoc
 *
 * Generate Spring REST Docs, Groovy Contract, Contract Test and Wiremock Stubs
 * Steps:
 * 1. Create REST API
 * 2. Write Integration Test for API by using (MockMVC, WebTestClient, RestAssured)
 * 3. In test pass the last line for Spring REST Docs generation with DSL e.g. .consumeWith(WebTestClientRestDocumentation.document("getbyid", SpringCloudContractRestDocs.dslContract()))
 * 4. Run Tests: 'gradle test --rerun-tasks'. It will create snippet directory in build and generate Groovy Contract DSL and JSON file for test
 * 5. Add directory 'asciidoc' under src->main and add index.adoc file which includes snippets generated while running tests
 * 6. Run Asciidoctor and Javadoc to generate documentation: 'gradle javadoc', 'gradle asciidoctor'. It will generate documentation in build directory
 * 7. Create 'contracts' directory in project->src->test->resources directory
 * 8. Copy all generated contracts in 'contracts' directory
 * 9. Generate Contract Tests: 'gradle generateContractTests'. It will generate contract tests in build directory
 * 10. Run and verify all contract tests are correct: 'contractTest'. In case modification needed in contract, fix it
 * 11. Generate Client Stub with Contract: 'gradle generateClientStubs'
 * 12. Verify Client Stub: 'gradle verifierStubsJar'
 * 13. Need to publish Jar & Stub to SCM/Local Maven Repository: 'gradle publish' or 'gradle publishToMavenLocal' or 'gradle publishStubsToScm' based upon configuration
 * 14. Now Stub has been generated, tested and published for others to use by using JUnit tests based contracts
 *
 * Steps for Stub Consumer:
 * 1. Import stub dependency in project (maven dependency or gradle testImplementation)
 * 2. Write code for consumption of REST APIs added as stub in project
 * 3. Write the test for the REST endpoint/service which uses Producer Stub REST APIs
 * 4. Add stub in test runner by using annotation on Test class: @org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner(ids = ["com.sk:project6:0.0.1-SNAPSHOT:stubs"], stubsMode = org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode.CLASSPATH)
 * 5. Run the test
 */


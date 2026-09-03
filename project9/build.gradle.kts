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

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	implementation("org.springframework.boot:spring-boot-starter-websocket")

	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.boot:spring-boot-starter-activemq")
	//implementation("org.apache.kafka:kafka-streams")
	//implementation("org.springframework.kafka:spring-kafka")
	implementation("org.reactivestreams:reactive-streams")
	//implementation("io.netty:netty-all:4.0.9.Final")
	implementation("io.projectreactor:reactor-core")
	implementation("io.projectreactor.netty:reactor-netty")

	implementation("org.webjars:webjars-locator-core")
	implementation("org.webjars:sockjs-client:1.1.1")
	implementation("org.webjars:stomp-websocket:2.3.3")
	implementation("org.webjars:bootstrap:3.3.7")
	implementation("org.webjars:jquery:3.1.1-1")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.amqp:spring-rabbit-test")
	testImplementation("org.springframework.kafka:spring-kafka-test")
}

tasks.withType<KotlinCompile> {
	compilerOptions {
		freeCompilerArgs.set(listOf("-Xjsr305=strict"))
		jvmTarget.set(JvmTarget.JVM_25)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

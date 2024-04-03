import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	kotlin("plugin.jpa") version "1.9.23"

}

group = "com.ajouin"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2023.0.0"

dependencies {

	//openai
	implementation("com.aallam.openai:openai-client:3.7.0")
	implementation("io.ktor:ktor-client-java:2.3.9")

	//kotlin
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.8.0")

	//spring
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	//spring cloud
	implementation("org.springframework.cloud:spring-cloud-starter-bus-amqp")
	implementation("org.springframework.cloud:spring-cloud-starter-config")
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	//monitoring
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-registry-prometheus")

	//jackson
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	//slack
	implementation("com.slack.api:slack-api-client:1.32.1")

	//logging
	implementation("io.github.oshai:kotlin-logging-jvm:5.1.1")

	//aws
	implementation("io.awspring.cloud:spring-cloud-aws-starter")
	implementation("io.awspring.cloud:spring-cloud-aws-starter-s3")
	implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs")

	// test
	testImplementation("org.testcontainers:localstack")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.awaitility:awaitility")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
		mavenBom("io.awspring.cloud:spring-cloud-aws-dependencies:3.0.1")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

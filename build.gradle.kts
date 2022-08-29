import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  id("com.adarshr.test-logger") version "3.2.0"
  application
}

group = "ga.overfullstack"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  val http4kVersion: String by project
  implementation("org.http4k:http4k-core:$http4kVersion")
  implementation("org.http4k:http4k-serverless-lambda:$http4kVersion")
  implementation("org.http4k:http4k-format-moshi:$http4kVersion")
  implementation("org.slf4j:slf4j-api:1.7.36")
  val exposedVersion: String by project
  implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
  implementation("org.postgresql:postgresql:42.5.0")
  implementation("com.h2database:h2:2.1.212")

  runtimeOnly("org.apache.logging.log4j:log4j-slf4j18-impl:2.17.2")

  val powerMockVersion = "2.0.9"
  testImplementation("org.powermock:powermock-module-junit4:$powerMockVersion")
  testImplementation("org.powermock:powermock-api-mockito2:$powerMockVersion")
  testImplementation(platform("org.junit:junit-bom:5.9.0"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
}

java.sourceCompatibility = JavaVersion.VERSION_17

tasks {
  test {
    useJUnitPlatform()
    ignoreFailures = true
  }
  withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = JavaVersion.VERSION_17.toString()
      freeCompilerArgs = listOf("-Xuse-k2")
    }
  }
  testlogger {
    theme = com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA_PARALLEL
  }
}

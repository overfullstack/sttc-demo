import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  id("com.adarshr.test-logger") version "3.1.0"
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
  implementation("com.h2database:h2:2.1.212")

  runtimeOnly("org.apache.logging.log4j:log4j-slf4j18-impl:2.17.0")

  testImplementation("org.mockito:mockito-inline:4.3.1")
//  testImplementation("org.powermock:powermock-module-junit4:+")
//  testImplementation("org.powermock:powermock-api-mockito2:+")
  testImplementation(platform("org.junit:junit-bom:5.8.2"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
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
    }
  }
}

testlogger {
  setTheme("mocha")
  showExceptions = true
  showStackTraces = false
  showFullStackTraces = false
  showCauses = false
  slowThreshold = 2000
  showSummary = true
  showSimpleNames = true
  showPassed = true
  showSkipped = true
  showFailed = true
  showStandardStreams = false
  showPassedStandardStreams = false
  showSkippedStandardStreams = false
  showFailedStandardStreams = false
}

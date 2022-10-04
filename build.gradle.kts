import com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep.XML
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  id("com.adarshr.test-logger") version "3.2.0"
  id("com.diffplug.spotless") version "6.11.0"
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
  implementation("org.slf4j:slf4j-api:2.0.1")
  val exposedVersion: String by project
  implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
  implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
  implementation("org.postgresql:postgresql:42.5.0")
  implementation("com.h2database:h2:2.1.214")

  runtimeOnly("org.apache.logging.log4j:log4j-slf4j18-impl:2.18.0")

  val powerMockVersion = "2.0.9"
  testImplementation("org.powermock:powermock-module-junit4:$powerMockVersion")
  testImplementation("org.powermock:powermock-api-mockito2:$powerMockVersion")
  testImplementation(platform("org.junit:junit-bom:5.9.0"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
}

java.sourceCompatibility = JavaVersion.VERSION_11

tasks {
  test {
    useJUnitPlatform()
    ignoreFailures = true
  }
  withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = JavaVersion.VERSION_11.toString()
      freeCompilerArgs = listOf("-Xuse-k2")
    }
  }
  testlogger {
    theme = com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA_PARALLEL
  }
}
spotless {
  kotlin {
    target("src/main/java/**/*.kt", "src/test/java/**/*.kt")
    targetExclude("$buildDir/generated/**/*.*")
    ktlint()
      .setUseExperimental(true)
      .editorConfigOverride(mapOf("indent_size" to "2", "continuation_indent_size" to "2"))
  }
  kotlinGradle {
    target("*.gradle.kts")
    ktlint()
      .setUseExperimental(true)
      .editorConfigOverride(mapOf("indent_size" to "2", "continuation_indent_size" to "2"))
  }
  java {
    toggleOffOn()
    target("src/main/java/**/*.java", "src/test/java/**/*.java")
    targetExclude("$buildDir/generated/**/*.*")
    importOrder()
    removeUnusedImports()
    googleJavaFormat()
    trimTrailingWhitespace()
    indentWithSpaces(2)
    endWithNewline()
  }
  format("xml") {
    targetExclude("pom.xml")
    target("*.xml")
    eclipseWtp(XML)
  }
  format("documentation") {
    target("*.md", "*.adoc")
    trimTrailingWhitespace()
    indentWithSpaces(2)
    endWithNewline()
  }
}

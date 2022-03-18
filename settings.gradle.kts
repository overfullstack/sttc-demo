pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
  val kotlinVersion: String by settings
  plugins {
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
  }
}

rootProject.name = "ppp-demo"


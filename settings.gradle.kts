dependencyResolutionManagement {
  versionCatalogs { create("libs") { from(files("libs.versions.toml")) } }

  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}

rootProject.name = "sttc-demo"

include("legacy")

include("demo-now")

include("loki")

include("loki-dud")

include("demo-before")

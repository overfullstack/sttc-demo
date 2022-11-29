plugins {
  application
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
}
dependencies {
  testImplementation(platform("org.junit:junit-bom:5.9.1"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}

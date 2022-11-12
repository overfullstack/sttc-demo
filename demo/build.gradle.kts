dependencies {
  implementation(project(":legacy"))
  implementation(project(":loki"))
  implementation(libs.bundles.apache.log4j)
  implementation(libs.bundles.spring)

  val powerMockVersion = "2.0.9"
  testImplementation("org.powermock:powermock-module-junit4:$powerMockVersion")
  testImplementation("org.powermock:powermock-api-mockito2:$powerMockVersion")
  testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
  testImplementation(libs.spring.test)
  testImplementation(libs.assertj.core)
}

tasks.withType<Test> {
  jvmArgs(
    // 🤦🏻♂️ This is for PowerMock
    "--add-opens=java.base/java.lang=ALL-UNNAMED",
    "--add-opens=java.base/java.util=ALL-UNNAMED",
    "--add-opens=java.base/java.util.concurrent=ALL-UNNAMED"
  )
}

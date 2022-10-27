dependencies {
  implementation(project(":legacy"))
  implementation(libs.bundles.apache.log4j)

  val powerMockVersion = "2.0.9"
  testImplementation("org.powermock:powermock-module-junit4:$powerMockVersion")
  testImplementation("org.powermock:powermock-api-mockito2:$powerMockVersion")

  testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
}

// This is for PowerMock
tasks.withType<Test> {
  jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED", "--add-opens=java.base/java.util=ALL-UNNAMED", "--add-opens=java.base/java.util.concurrent=ALL-UNNAMED")
}

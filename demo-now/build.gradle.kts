dependencies {
  implementation(project(":loki"))
  implementation(libs.bundles.apache.log4j)
  implementation(libs.bundles.spring)
  testImplementation(libs.spring.test)
  testImplementation(libs.assertj.core)
}

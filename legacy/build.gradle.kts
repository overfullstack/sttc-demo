dependencies {
  api(libs.http4k.core)
  api(libs.http4k.format.moshi)
  implementation(libs.bundles.apache.log4j)
  api(libs.bundles.exposed)
  implementation(libs.postgresql)
}

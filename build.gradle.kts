import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.report.ReportMergeTask

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id(libs.plugins.kover.pluginId)
  id(libs.plugins.detekt.pluginId) apply false
}
allprojects {
  apply(plugin = "sttc.root-conventions")
  repositories {
    mavenCentral()
  }
}
koverReport { defaults { html { onCheck = true } } }
val detektReportMerge by tasks.registering(ReportMergeTask::class) {
  output.set(rootProject.buildDir.resolve("reports/detekt/merge.xml"))
}
subprojects {
  apply(plugin = "sttc.sub-conventions")
  apply(plugin = "sttc.kt-conventions")
  tasks.withType<Detekt>().configureEach {
    reports {
      html.required by true
    }
  }
  plugins.withType<DetektPlugin> {
    tasks.withType<Detekt> detekt@{
      finalizedBy(detektReportMerge)
      detektReportMerge.configure {
        input.from(this@detekt.xmlReportFile)
      }
    }
  }
}

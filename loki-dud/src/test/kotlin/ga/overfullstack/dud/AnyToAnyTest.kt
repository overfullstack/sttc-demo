package ga.overfullstack.dud

import ga.overfullstack.loki.dud.AnyToAny
import io.kotest.core.spec.style.StringSpec

class AnyToAnyTest : StringSpec({
   "Generate random map" {
     AnyToAny.getMap(this.testCase.name.testName, String::class.java, String::class.java, 3).forEach(::println)
   }
})

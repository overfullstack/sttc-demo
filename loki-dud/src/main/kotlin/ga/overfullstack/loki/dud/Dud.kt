package ga.overfullstack.loki.dud

import com.google.common.collect.HashBasedTable
import ga.overfullstack.loki.Utils
import ga.overfullstack.loki.Utils.computeIfAbsent
import org.junit.jupiter.api.Test
import java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE
import java.lang.StackWalker.StackFrame

object Dud {

  private val keyToTable = mutableMapOf<Any, HashBasedTable<Any, Any, Any>>()
  private val stackWalker = StackWalker.getInstance(RETAIN_CLASS_REFERENCE)

  @JvmStatic
  fun clear() {
    MultiAnyToAny.clear()
    keyToTable.clear()
  }

  private fun <T : Annotation> findFirstMethodWithAnnotationFromStackTrace(type: Class<T>): String =
    stackWalker.walk { stackFrames ->
      stackFrames.filter { stackFrame ->
        doesMethodHasAnnotation(
          type,
          stackFrame
        )
      }.findFirst().map { it.methodName }
    }.orElseThrow { IllegalCallerException("No method found with @Test annotation") };

  private fun <T : Annotation> doesMethodHasAnnotation(
    annotationClass: Class<T>,
    stackFrame: StackFrame
  ): Boolean =
    stackFrame.declaringClass
      .getDeclaredMethod(stackFrame.methodName, *stackFrame.methodType.parameterArray())
      .getAnnotation(annotationClass) != null

  /**
   * Get and Generate are placed in the same method in-order to ease it for the client to avoid checking for `isPresent`
   */
  @JvmStatic
  fun <T : Any> getOrGenerateValueIfAbsent( // ! TODO 26/12/22 gopala.akshintala: Avoid multiple method calls for default params 
    valueType: Class<T>?,
    dudKey: Any?,
    entryKey: Any?,
    propKey: Any?
  ): T { // ! TODO 27/12/22 gopala.akshintala: Should I send auto generated dudKey too?
    val keyFromTestName = findFirstMethodWithAnnotationFromStackTrace(Test::class.java)
    return keyToTable.computeIfAbsent(dudKey ?: keyFromTestName) { HashBasedTable.create() }
      .computeIfAbsent(entryKey?: keyFromTestName, propKey ?: keyFromTestName, valueType) { _, _ ->
        Utils.randomForPrimitiveType(
          valueType!!
        )
      } as T
  }

  // ! TODO 27/12/22 gopala.akshintala: Turn these into builders 
  @JvmStatic
  fun getOrGenerateTableIfAbsent( // ! TODO 26/12/22 gopala.akshintala: Avoid multiple method calls for default params 
    dudKey: Any?,
    noOfEntriesToGenerate: Int?,
    propKeyToValueType: Map<Any, Class<*>>?,
    entryKeyPrefix: Any?
  ): HashBasedTable<Any, Any, Any> {
    val keyFromTestName = findFirstMethodWithAnnotationFromStackTrace(Test::class.java)
    val table = keyToTable.computeIfAbsent(dudKey ?: keyFromTestName) { HashBasedTable.create() }
    noOfEntriesToGenerate?.let {
      repeat(it) { index ->
        propKeyToValueType!!.entries.forEach { (propKey, valueType) ->
          // ! TODO 27/12/22 gopala.akshintala: If Entry prefix already present with some indexes? 
          // * NOTE 27/12/22 gopala.akshintala: rowKey is of type `Pair` only when it is generated 
          table.computeIfAbsent(Pair(entryKeyPrefix ?: keyFromTestName, index), propKey, valueType) { _, _ ->
            Utils.randomForPrimitiveType(
              valueType
            )
          }
        }
      }
    }
    return table
  }

  /**
   * This overwrites the value (if any) previously set for this key
   */
  @JvmStatic
  fun put(
    dudKey: Any?,
    entryKey: Any,
    propKey: Any,
    value: Any
  ) {
    val keyFromTestName = findFirstMethodWithAnnotationFromStackTrace(Test::class.java)
    keyToTable.computeIfAbsent(dudKey ?: keyFromTestName) { HashBasedTable.create() }
      .put(entryKey, propKey, value)
  }

}

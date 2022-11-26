package ga.overfullstack.loki

import com.google.common.collect.HashBasedTable
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.commons.lang3.RandomStringUtils.randomNumeric
import org.mockito.Mockito
import java.util.function.BiFunction

object Utils {
  private const val RANDOM_STRING_LENGTH = 18
  private const val RANDOM_NUMERIC_LENGTH = 3
  fun <T : Any> randomForPrimitiveType(type: Class<T>): T = when (type) {
    String::class.java -> randomAlphabetic(RANDOM_STRING_LENGTH)
    Int::class.javaObjectType -> randomNumeric(RANDOM_NUMERIC_LENGTH).toInt()
    Long::class.javaObjectType -> randomNumeric(RANDOM_NUMERIC_LENGTH).toLong()
    Double::class.javaObjectType -> randomNumeric(RANDOM_NUMERIC_LENGTH).toDouble()
    Float::class.javaObjectType -> randomNumeric(RANDOM_NUMERIC_LENGTH).toFloat()
    Boolean::class.javaObjectType -> true
    else -> Mockito.mock(type)
  } as T

  fun <R : Any, C : Any, V : Any> HashBasedTable<R, C, V>.computeIfAbsent(
    rowKey: R,
    colKey: C,
    mappingFunction: BiFunction<in R, in C, out V?>
  ): V? =
    if (!contains(rowKey, colKey)) {
      val newValue = mappingFunction.apply(rowKey, colKey)
      if (newValue != null) {
        put(rowKey, colKey, newValue)
      }
      newValue
    } else get(rowKey, colKey)
}

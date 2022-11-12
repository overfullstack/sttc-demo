package ga.overfullstack.loki

import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.commons.lang3.RandomStringUtils.randomNumeric
import org.mockito.Mockito

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
}

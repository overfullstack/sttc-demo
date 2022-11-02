@file:JvmName("MultiAnyToAny")

package ga.overfullstack.loki.dud

import ga.overfullstack.loki.Utils.randomForPrimitiveType
import org.mockito.Mockito

private val pairOfAnyToAnyCache = mutableMapOf<Pair<Any, Any>, Any?>()

fun <T : Any> get(key1: Any, key2: Any, valueType: Class<T>): T =
  pairOfAnyToAnyCache.computeIfAbsent(Pair(key1, key2)) { randomForPrimitiveType(valueType) } as T

fun get(key1: Any, key2: Any): Any? =
  pairOfAnyToAnyCache.computeIfAbsent(Pair(key1, key2)) { Mockito.mock(Any::class.java) }

fun put(key1: Any, key2: Any, value: Any?) {
  pairOfAnyToAnyCache[Pair(key1, key2)] = value
}

@file:JvmName("MultiAnyToAny")

package ga.overfullstack.loki.dud

import ga.overfullstack.loki.Utils.randomForPrimitiveType
import org.mockito.Mockito

object MultiAnyToAny {
  private val pairOfAnyToAnyCache = mutableMapOf<Pair<Any, Any>, Any?>()

  // ! FIXME gopala.akshintala 05/06/22: Think about primitive types
  @JvmStatic
  fun <T : Any> get(key1: Any, key2: Any, valueType: Class<T>): T =
    pairOfAnyToAnyCache.computeIfAbsent(Pair(key1, key2)) { randomForPrimitiveType(valueType) } as T

  @JvmStatic
  fun get(key1: Any, key2: Any): Any? =
    pairOfAnyToAnyCache.computeIfAbsent(Pair(key1, key2)) { Mockito.mock(Any::class.java) }

  @JvmStatic
  fun put(key1: Any, key2: Any, value: Any?) {
    pairOfAnyToAnyCache[Pair(key1, key2)] = value
  }

  @JvmStatic
  fun <K : Any, P : Any, V : Any?> getCache(): Map<K, Map<P, V>> = pairOfAnyToAnyCache.map { (key, value) -> Triple(key.first as K, key.second as P, value as V) }
    .groupBy({ it.first }, { Pair(it.second, it.third) }).mapValues { (_, value) -> value.toMap() }
}

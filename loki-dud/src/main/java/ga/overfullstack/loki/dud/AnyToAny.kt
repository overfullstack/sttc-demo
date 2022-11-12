@file:JvmName("AnyToAny")

package ga.overfullstack.loki.dud

import ga.overfullstack.loki.Utils.randomForPrimitiveType

object AnyToAny {
  private val anyToAnyCache = mutableMapOf<Any, Any?>()

  @JvmStatic
  fun <T : Any> get(key: Any, valueType: Class<T>): T =
    anyToAnyCache.computeIfAbsent(key) { randomForPrimitiveType(valueType) } as T

  @JvmStatic
  fun <K : Any, V : Any> getMap(key: Any, keyType: Class<K>, valueType: Class<V>, size: Int): Map<K, V> =
    anyToAnyCache.computeIfAbsent(key) {
      buildMap(size) {
        repeat(size) {
          this[randomForPrimitiveType(keyType)] = randomForPrimitiveType(valueType)
        }
      }
    } as Map<K, V>

  @JvmStatic
  fun <K : Any, V : Any> getMap(key: Any): Map<K, V> = (anyToAnyCache[key] ?: emptyMap<K, V>()) as Map<K, V>

  @JvmStatic
  fun put(key: Any, value: Any) {
    anyToAnyCache[key] = value
  }
}

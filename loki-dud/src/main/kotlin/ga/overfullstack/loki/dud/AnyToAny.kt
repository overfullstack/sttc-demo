package ga.overfullstack.loki.dud

import ga.overfullstack.loki.Utils.randomForPrimitiveType

object AnyToAny {
  private val anyToAnyCache = mutableMapOf<Any, Any?>()

  @JvmStatic
  fun <T : Any> get(key: Any, valueType: Class<T>): T =
    anyToAnyCache.computeIfAbsent(key) { randomForPrimitiveType(valueType) } as T

  @JvmStatic
  fun <K : Any, V : Any> getMap(
    key: Any,
    keyType: Class<K>,
    valueType: Class<V>,
    resultSize: Int
  ): MutableMap<K, V> =
    anyToAnyCache.computeIfAbsent(key) {
      buildMap(resultSize) {
          repeat(resultSize) {
            this[randomForPrimitiveType(keyType)] = randomForPrimitiveType(valueType)
          }
        }
        .toMutableMap()
    } as MutableMap<K, V>

  @JvmStatic
  fun <K : Any, V : Any> getMap(key: Any): MutableMap<K, V> =
    (anyToAnyCache[key] ?: mutableMapOf<K, V>()) as MutableMap<K, V>

  @JvmStatic
  fun put(key: Any, value: Any) {
    anyToAnyCache[key] = value
  }

  fun clear() {
    anyToAnyCache.clear()
  }
}

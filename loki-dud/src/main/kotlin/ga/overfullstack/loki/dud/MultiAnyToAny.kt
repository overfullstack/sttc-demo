package ga.overfullstack.loki.dud

import com.google.common.collect.HashBasedTable
import ga.overfullstack.loki.Utils.computeIfAbsent
import ga.overfullstack.loki.Utils.randomForPrimitiveType
import org.mockito.Mockito

object MultiAnyToAny {
  private val table = HashBasedTable.create<Any, Any, Any?>()

  @JvmStatic
  fun <T : Any> get(key1: Any, key2: Any, valueType: Class<T>): T =
    table.computeIfAbsent(key1, key2) { _, _ -> randomForPrimitiveType(valueType) } as T

  @JvmStatic
  fun get(key1: Any, key2: Any): Any? =
    table.computeIfAbsent(key1, key2) { _, _ -> Mockito.mock(Any::class.java) }

  @JvmStatic
  fun put(key1: Any, key2: Any, value: Any) {
    table.put(key1, key2, value)
  }

  @JvmStatic
  fun <K : Any, P : Any, V : Any?> getRows(): MutableMap<K, MutableMap<P, V>> =
    table.rowMap() as MutableMap<K, MutableMap<P, V>>

  fun clear() {
    table.clear()
  }
}

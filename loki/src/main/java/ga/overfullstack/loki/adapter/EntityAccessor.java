package ga.overfullstack.loki.adapter;

import ga.overfullstack.legacy.Entity;
import ga.overfullstack.legacy.EntityLoader;
import ga.overfullstack.legacy.LoadFromDBException;

/**
 * Port and Prod Adapter together (Thanks to `default` interface methods in Java). This is just a
 * wrapper around the Legacy Entity and Delegates to it. This acts as a "Level of Indirection"
 */
public interface EntityAccessor {
  default <T extends Entity> T loadNew(Class<T> type) throws LoadFromDBException {
    return EntityLoader.loadNew(type);
  }

  default String get(Entity entity, String field) {
    return entity.get(field);
  }

  default void put(Entity entity, String field, String value) {
    entity.put(field, value);
  }
}

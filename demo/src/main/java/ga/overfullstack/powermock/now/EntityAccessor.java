package ga.overfullstack.powermock.now;

import ga.overfullstack.legacy.Entity;
import ga.overfullstack.legacy.EntityLoader;

/**
 * Port and Prod Adapter together (Thanks to `default` interface methods in Java). This is just a wrapper around the
 * Legacy Entity and Delegates to it
 */
public interface EntityAccessor {
  default <T extends Entity> T loadNew(Class<T> type) {
    return EntityLoader.loadNew(type);
  }

  default String get(Entity entity, String field) {
    return entity.get(field);
  }

  default void put(Entity entity, String field, String value) {
    entity.put(field, value);
  }
  
}

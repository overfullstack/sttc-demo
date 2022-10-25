package ga.overfullstack.powermock.after;

import ga.overfullstack.powermock.Entity;
import ga.overfullstack.powermock.EntityLoader;

/**
 * Port and Prod Adapter (Thanks to Default methods in Java)
 */
public interface EntityAccessor {
  default <T extends Entity> T loadNew(Class<T> type) {
    return EntityLoader.loadNew(type);
  }

  default void put(Entity entity, String field, String value) {
    entity.put(field, value);
  }

  default String get(Entity entity, String field) {
    return entity.get(field);
  }
}

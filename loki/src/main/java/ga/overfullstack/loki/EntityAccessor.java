package ga.overfullstack.loki;

import static ga.overfullstack.loki.BeanName.ENTITY_ACCESSOR_LOKI;

import ga.overfullstack.legacy.Entity;
import ga.overfullstack.legacy.EntityLoader;
import ga.overfullstack.legacy.LoadFromDBException;
import org.springframework.stereotype.Component;

/**
 * Port and Prod Adapter together (Thanks to `default` interface methods in Java). This is just a
 * wrapper around the Legacy Entity and Delegates to it. This acts as a "Level of Indirection"
 */
@Component(ENTITY_ACCESSOR_LOKI)
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

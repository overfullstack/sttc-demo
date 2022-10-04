package ga.overfullstack.powermock;

import java.lang.reflect.InvocationTargetException;

/** Dummy class representing loading entity from DB */
public class EntityLoader {
  private EntityLoader() {}

  public static <T extends Entity> T loadNew(Class<T> entityType) {
    try {
      return entityType.getConstructor().newInstance();
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }
}

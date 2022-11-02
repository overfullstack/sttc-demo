package ga.overfullstack.legacy;

import java.lang.reflect.InvocationTargetException;

/** Dummy class representing loader to load entity from DB */
public class EntityLoader {
  private EntityLoader() {}

  public static <T extends Entity> T loadNew(Class<T> entityType) throws LoadFromDBException {
    try {
      return entityType.getConstructor().newInstance();
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      throw new LoadFromDBException(e); // Just for Demo
    }
  }
}

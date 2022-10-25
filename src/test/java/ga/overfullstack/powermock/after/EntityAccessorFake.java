package ga.overfullstack.powermock.after;

import ga.overfullstack.powermock.Entity;
import kotlin.Pair;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

/**
 * Fake Adapter
 */
public class EntityAccessorFake implements EntityAccessor {

  private final Map<Pair<?, ?>, Object> cache = new HashMap<>();

  @Override
  public <T extends Entity> T loadNew(Class<T> type) {
    return Mockito.mock(type);
  }

  @Override
  public void put(Entity entity, String field, String value) {
    cache.put(new Pair<>(entity, field), value);
  }

  @Override
  public String get(Entity entity, String field) {
    return (String) cache.get(new Pair<>(entity, field));
  }
}

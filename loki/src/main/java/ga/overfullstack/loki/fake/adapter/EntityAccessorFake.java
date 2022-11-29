package ga.overfullstack.loki.fake.adapter;

import static ga.overfullstack.loki.fake.BeanName.ENTITY_ACCESSOR_LOKI_FAKE;

import ga.overfullstack.legacy.Entity;
import ga.overfullstack.loki.adapter.EntityAccessor;
import ga.overfullstack.loki.dud.MultiAnyToAny;
import org.mockito.Mockito;
import org.springframework.stereotype.Component;

/** Fake Adapter */
@Component(ENTITY_ACCESSOR_LOKI_FAKE)
public class EntityAccessorFake implements EntityAccessor {

  @Override
  public <T extends Entity> T loadNew(Class<T> type) {
    return Mockito.mock(type);
  }

  @Override
  public void put(Entity entity, String field, String value) {
    MultiAnyToAny.put(entity, field, value);
  }

  @Override
  public String get(Entity entity, String field) {
    return MultiAnyToAny.get(entity, field, String.class);
  }
}

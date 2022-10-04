package ga.overfullstack.powermock.after;

import ga.overfullstack.powermock.Entity;
import ga.overfullstack.powermock.Pokemon;
import java.util.HashMap;
import java.util.Map;
import kotlin.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BeanToEntityTest {

  @Test
  void toEntity() {
    final var entityAccessorFake = new EntityAccessorFake() {};
    final var beanToEntity = new BeanToEntity(entityAccessorFake);
    final var pokemonBean = new Pokemon("mockPokemon", "mockPower");
    final var pokemonEntity = beanToEntity.toEntity(pokemonBean);
    Assertions.assertEquals(entityAccessorFake.get(pokemonEntity, "name"), pokemonBean.getName());
    Assertions.assertEquals(entityAccessorFake.get(pokemonEntity, "power"), pokemonBean.getPower());
  }
}

interface EntityAccessorFake extends EntityAccessor {

  Map<Pair<? extends Entity, String>, String> cache = new HashMap<>();

  @Override
  default <T extends Entity> T loadNew(Class<T> type) {
    return Mockito.mock(type);
  }

  @Override
  default void put(Entity entity, String field, String value) {
    cache.put(new Pair<>(entity, field), value);
  }

  @Override
  default String get(Entity entity, String field) {
    return cache.get(new Pair<>(entity, field));
  }
}

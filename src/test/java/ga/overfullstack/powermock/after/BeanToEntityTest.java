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
    final var entityAccessorFake = new EntityAccessorFake();
    // 💉 Inject the fake adapter instance
    final var beanToEntity = new BeanToEntity(entityAccessorFake);
    // Given
    final var pokemonBean = new Pokemon("mockPokemon", "mockPower");
    // When
    final var pokemonEntity = beanToEntity.toEntity(pokemonBean);
    // Then
    Assertions.assertEquals(entityAccessorFake.get(pokemonEntity, "name"), pokemonBean.getName());
    Assertions.assertEquals(entityAccessorFake.get(pokemonEntity, "power"), pokemonBean.getPower());
  }
}


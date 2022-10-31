package ga.overfullstack.powermock.now;

import ga.overfullstack.powermock.Pokemon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

class BeanToEntityTest {

  @Test
  void toEntity() {
    final var entityAccessorFake = new EntityAccessorFake();
    // 💉 Inject the fake adapter instances
    final var beanToEntity = new BeanToEntity(entityAccessorFake, Mockito.mock(Logger.class));
    // Given
    final var pokemonBean = new Pokemon("mockPokemon", "mockPower");
    // When
    final var pokemonEntity = beanToEntity.toEntity(pokemonBean);
    // Then
    Assertions.assertEquals(entityAccessorFake.get(pokemonEntity, "name"), pokemonBean.getName());
    Assertions.assertEquals(entityAccessorFake.get(pokemonEntity, "power"), pokemonBean.getPower());
  }
}

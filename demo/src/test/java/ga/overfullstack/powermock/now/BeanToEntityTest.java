package ga.overfullstack.powermock.now;

import ga.overfullstack.legacy.LoadFromDBException;
import ga.overfullstack.loki.fake.EntityAccessorFake;
import ga.overfullstack.powermock.Pokemon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

class BeanToEntityTest {

  @Test
  void toEntity() throws LoadFromDBException {
    final var entityAccessorFake = new EntityAccessorFake();
    // 💉 Inject the fake adapter instances
    final var beanToEntity = new BeanToEntity(entityAccessorFake, Mockito.mock(Logger.class));
    // Given
    final var pokemonBean = new Pokemon(null, "mockPower");
    // When
    final var pokemonEntity = beanToEntity.toEntity(pokemonBean);
    // Then
    Assertions.assertEquals("", entityAccessorFake.get(pokemonEntity, "name"));
    Assertions.assertEquals(pokemonBean.power(), entityAccessorFake.get(pokemonEntity, "power"));
  }
}

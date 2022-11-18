package ga.overfullstack.pokemon.now;

import static ga.overfullstack.loki.fake.BeanName.ENTITY_ACCESSOR_LOKI_FAKE;

import ga.overfullstack.legacy.LoadFromDBException;
import ga.overfullstack.loki.EntityAccessor;
import ga.overfullstack.loki.dud.Dud;
import ga.overfullstack.loki.fake.LokiConfigForTest;
import ga.overfullstack.pokemon.Pokemon;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LokiConfigForTest.class, BeanToEntityMapper.class})
class BeanToEntityMapperMapperTest {

  @Autowired
  BeanToEntityMapper beanToEntityMapper;

  @Autowired
  @Qualifier(ENTITY_ACCESSOR_LOKI_FAKE)
  EntityAccessor entityAccessorFake;

  @AfterEach
  void afterEach() {
    Dud.clear();
  }

  @Test
  void updateInDB() throws LoadFromDBException {
    // Given
    final var pokemonBean = new Pokemon(null, "mockPower");
    // When
    final var pokemonEntity = beanToEntityMapper.insertInDB(pokemonBean);
    // Then
    Assertions.assertEquals("", entityAccessorFake.get(pokemonEntity, "name"));
    Assertions.assertEquals(pokemonBean.power(), entityAccessorFake.get(pokemonEntity, "power"));
  }
}

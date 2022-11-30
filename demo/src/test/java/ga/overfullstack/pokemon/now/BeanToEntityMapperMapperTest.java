package ga.overfullstack.pokemon.now;

import static ga.overfullstack.loki.fake.BeanName.ENTITY_ACCESSOR_LOKI_FAKE;
import static ga.overfullstack.loki.fake.BeanName.LOGGER_NO_OP_SUPPLIER_LOKI;

import ga.overfullstack.legacy.LoadFromDBException;
import ga.overfullstack.loki.adapter.EntityAccessor;
import ga.overfullstack.loki.adapter.LoggerSupplier;
import ga.overfullstack.loki.dud.Dud;
import ga.overfullstack.loki.fake.adapter.LokiConfigForTest;
import ga.overfullstack.pokemon.Pokemon;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LokiConfigForTest.class})
class BeanToEntityMapperMapperTest {

  BeanToEntityMapper beanToEntityMapper;

  @Autowired
  @Qualifier(LOGGER_NO_OP_SUPPLIER_LOKI)
  LoggerSupplier loggerNoOpSupplier;

  @Autowired
  @Qualifier(ENTITY_ACCESSOR_LOKI_FAKE)
  EntityAccessor entityAccessorFake;

  @AfterEach
  void afterEach() {
    Dud.clear();
  }

  @BeforeEach
  void setUp() {
    beanToEntityMapper = new BeanToEntityMapper(entityAccessorFake, loggerNoOpSupplier);
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

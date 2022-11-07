package ga.overfullstack.powermock.now;

import ga.overfullstack.legacy.LoadFromDBException;
import ga.overfullstack.loki.CoreConfig;
import ga.overfullstack.loki.EntityAccessor;
import ga.overfullstack.loki.LoggerSupplier;
import ga.overfullstack.loki.fake.CoreFakeConfig;
import ga.overfullstack.loki.fake.EntityAccessorFake;
import ga.overfullstack.powermock.Pokemon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static ga.overfullstack.loki.fake.BeanName.ENTITY_ACCESSOR_LOKI_FAKE;
import static ga.overfullstack.loki.fake.BeanName.LOGGER_NO_OP_SUPPLIER_LOKI;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CoreFakeConfig.class)
class BeanToEntityTest {

  @Autowired
  @Qualifier(ENTITY_ACCESSOR_LOKI_FAKE)
  EntityAccessor entityAccessorFake;

  @Autowired
  @Qualifier(LOGGER_NO_OP_SUPPLIER_LOKI)
  LoggerSupplier loggerNoOpSupplier;
  
  @Test
  void updateInDB() throws LoadFromDBException {
    // 💉 Inject the fake adapter instances
    final var beanToEntity = new BeanToEntity(entityAccessorFake, loggerNoOpSupplier);
    // Given
    final var pokemonBean = new Pokemon(null, "mockPower");
    // When
    final var pokemonEntity = beanToEntity.updateInDB(pokemonBean);
    // Then
    Assertions.assertEquals("", entityAccessorFake.get(pokemonEntity, "name"));
    Assertions.assertEquals(pokemonBean.power(), entityAccessorFake.get(pokemonEntity, "power"));
  }
}

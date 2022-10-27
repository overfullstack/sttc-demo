package ga.overfullstack.powermock.before;

import static org.junit.Assert.assertEquals;

import ga.overfullstack.legacy.EntityLoader;
import ga.overfullstack.powermock.Pokemon;
import ga.overfullstack.powermock.PowerEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EntityLoader.class})
@PowerMockIgnore({
  "com.sun.org.apache.xerces.*",
  "javax.xml.*",
  "org.xml.*",
  "org.w3c.dom.*",
  "javax.management.*",
  "javax.crypto.JceSecurity.*",
  "javax.crypto.*"
})
public class BeanToEntityTest {

  /**
   * This test achieves 100% coverage without testing anything This doesn't test the behavior which
   * is mapping fields from `pokemonBean` to `pokemonEntity` Instead, it returns a Mock on
   * `loadNew`, does nothing on `put`, and asserts on the same Mock to make the test pass
   */
  @Test
  public void toEntity() {
    PowerMockito.mockStatic(EntityLoader.class);
    // 👹 Return a Mock entity on `loadNew`
    final var mockPokemonEntity = PowerMockito.mock(PowerEntity.class);
    PowerMockito.when(EntityLoader.loadNew(PowerEntity.class)).thenReturn(mockPokemonEntity);

    // 👹 Do Nothing on `put`
    PowerMockito.doNothing()
        .when(mockPokemonEntity)
        .put(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

    final var pokemonBean = new Pokemon("mockPokemon", "mockPower");
    PowerMockito.when(mockPokemonEntity.get("name")).thenReturn(pokemonBean.getName());
    PowerMockito.when(mockPokemonEntity.get("power")).thenReturn(pokemonBean.getPower());

    final var actualPokemonEntity = BeanToEntity.toEntity(pokemonBean);

    // 👹 Assert on the same Mock entity from above
    assertEquals("mockPokemon", actualPokemonEntity.get("name"));
    assertEquals("mockPower", actualPokemonEntity.get("power"));
  }
}

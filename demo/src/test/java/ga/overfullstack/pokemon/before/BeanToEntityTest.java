package ga.overfullstack.pokemon.before;

import static org.junit.Assert.assertEquals;

import ga.overfullstack.legacy.Entity;
import ga.overfullstack.legacy.EntityLoader;
import ga.overfullstack.legacy.LoadFromDBException;
import ga.overfullstack.pokemon.Pokemon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EntityLoader.class})
@PowerMockIgnore({ // To make PowerMock work with the latest Java version
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
   * `loadNew`, does nothing on `put`, does `when-thenReturn` on the Mock and asserts the same
   * values to make the test pass Although this gives 100% test coverage, this doesn't detect a bug
   * 🐞 in the code
   */
  @Test
  public void updateInDB() throws LoadFromDBException {
    PowerMockito.mockStatic(EntityLoader.class);
    // 👹 Return a Mock entity on `loadNew`
    final var mockPokemonEntity = PowerMockito.mock(Entity.class);
    PowerMockito.when(EntityLoader.loadNew(Entity.class)).thenReturn(mockPokemonEntity);

    // 👹 Do Nothing on `put`
    PowerMockito.doNothing()
        .when(mockPokemonEntity)
        .put(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

    final var pokemonBean = new Pokemon(null, "mockPower");
    PowerMockito.when(mockPokemonEntity.get("name")).thenReturn("");
    PowerMockito.when(mockPokemonEntity.get("power")).thenReturn(pokemonBean.power());

    final var actualPokemonEntity = BeanToEntity.insertInDB(pokemonBean);

    // 👹 Assert on the same Mock entity from above
    assertEquals("", actualPokemonEntity.get("name"));
    assertEquals("mockPower", actualPokemonEntity.get("power"));
  }
}

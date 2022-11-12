package ga.overfullstack.pokemon.before;

import static ga.overfullstack.pokemon.before.App.POKEMON_LIMIT_TO_FETCH;
import static ga.overfullstack.pokemon.before.App.POKEMON_OFFSET_TO_FETCH;
import static org.powermock.api.mockito.PowerMockito.when;

import ga.overfullstack.legacy.DBUtil;
import ga.overfullstack.legacy.HttpUtil;
import ga.overfullstack.legacy.LoadFromDBException;
import ga.overfullstack.pokemon.Pokemon;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * There is a lot of effort to test with PowerMock
 * <li>Prepare classes for Test
 * <li>Suppress unwanted functionality
 * <li>Workarounds to work with Java 11 through PowerMockIgnore
 * <li>MockStatic methods
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DBUtil.class, HttpUtil.class, BeanToEntity.class, LoggerFactory.class})
@SuppressStaticInitializationFor("ga.overfullstack.legacy.DBUtil")
@PowerMockIgnore({
  "com.sun.org.apache.xerces.*",
  "javax.xml.*",
  "org.xml.*",
  "org.w3c.dom.*",
  "javax.management.*",
  "javax.crypto.JceSecurity.*",
  "javax.crypto.*"
})
public class PokemonCollectorTest {
  @Before
  public void setUp() {
    PowerMockito.mockStatic(DBUtil.class);
    PowerMockito.mockStatic(HttpUtil.class);
    PowerMockito.mockStatic(BeanToEntity.class);
    PowerMockito.mockStatic(LoggerFactory.class);
    PowerMockito.when(LoggerFactory.getLogger(ArgumentMatchers.any(Class.class)))
        .thenAnswer(ignore -> PowerMockito.mock(Logger.class));
  }

  @Test
  public void play() throws LoadFromDBException {
    final var pokemonFromNetworkFake = List.of("pokemon1", "pokemon2", "pokemon3");
    when(HttpUtil.fetchAllPokemonNames(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
        .thenAnswer(ignore -> pokemonFromNetworkFake);
    final var pokemonWithPowerFromDBFake =
        Map.of(
            "pokemon1", "power1",
            "pokemon3", "power3");
    when(DBUtil.queryPokemonPowers(pokemonFromNetworkFake))
        .thenAnswer(ignore -> pokemonWithPowerFromDBFake);
    when(HttpUtil.fetchPokemonPower("pokemon2")).thenAnswer(ignore -> "power2");

    PokemonCollector.play(POKEMON_OFFSET_TO_FETCH, POKEMON_LIMIT_TO_FETCH);

    PowerMockito.verifyStatic(BeanToEntity.class);
    BeanToEntity.insertInDB(new Pokemon("pokemon2", "power2"));
  }
}

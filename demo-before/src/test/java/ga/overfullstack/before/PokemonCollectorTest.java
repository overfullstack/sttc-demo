package ga.overfullstack.before;

import static ga.overfullstack.before.PokemonCollector.POKEMON_LIMIT_TO_FETCH;
import static ga.overfullstack.before.PokemonCollector.POKEMON_OFFSET_TO_FETCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.when;

import ga.overfullstack.legacy.DBUtil;
import ga.overfullstack.legacy.HttpUtil;
import ga.overfullstack.legacy.LoadFromDBException;
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
 * It takes a lot of effort to test with PowerMock
 * <li>Prepare classes for Test
 * <li>Suppress unwanted functionality
 * <li>Workarounds to work with Java 11 through PowerMockIgnore
 * <li>MockStatic methods
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DBUtil.class, HttpUtil.class, BeanToEntityMapper.class, LoggerFactory.class})
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
    PowerMockito.mockStatic(BeanToEntityMapper.class);
    PowerMockito.mockStatic(LoggerFactory.class);
    PowerMockito.when(LoggerFactory.getLogger(ArgumentMatchers.any(Class.class)))
        .thenAnswer(ignore -> PowerMockito.mock(Logger.class));
  }

  @Test
  public void collectPokemon() throws LoadFromDBException {
    final var pokemonFromNetworkFake = List.of("pokemon1", "pokemon2", "pokemon3");
    when(HttpUtil.fetchAllPokemonNames(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
        .thenAnswer(ignore -> pokemonFromNetworkFake);
    when(DBUtil.queryPokemonPowers(pokemonFromNetworkFake))
        .thenAnswer(ignore -> Map.of("pokemon2", "power2"));
    final var expectedResult =
        Map.of(
            "pokemon1", "power1",
            "pokemon2", "power2",
            "pokemon3", "power3");
    // 👹 Set the expected result
    when(DBUtil.queryAllPokemonWithPowers()).thenAnswer(ignore -> expectedResult);
    when(HttpUtil.fetchPokemonPower("pokemon1"))
        .thenAnswer(ignore -> expectedResult.get("pokemon1"));
    when(HttpUtil.fetchPokemonPower("pokemon3"))
        .thenAnswer(ignore -> expectedResult.get("pokemon3"));

    final var result = PokemonCollector.play(POKEMON_OFFSET_TO_FETCH, POKEMON_LIMIT_TO_FETCH);

    // Verify specific `static` method interactions
    PowerMockito.verifyStatic(BeanToEntityMapper.class, times(1));
    BeanToEntityMapper.insertInDB(new PokemonCollector.Pokemon("pokemon1", "power1"));
    PowerMockito.verifyStatic(BeanToEntityMapper.class, times(1));
    BeanToEntityMapper.insertInDB(new PokemonCollector.Pokemon("pokemon3", "power3"));
    PowerMockito.verifyNoMoreInteractions(BeanToEntityMapper.class);

    // 👹 Assert the same result set through `when-then` above
    assertThat(result).containsExactlyInAnyOrderEntriesOf(expectedResult);
  }
}

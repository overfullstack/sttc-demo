package ga.overfullstack.pokemon.before;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.List;
import java.util.Map;
import kotlin.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DBUtil.class, HttpUtil.class, LoggerFactory.class})
@SuppressStaticInitializationFor("ga.overfullstack.pokemon.before.DBUtil")
public class PokemonDemoPowerMockTest {
  @Test
  public void play() {
    PowerMockito.mockStatic(DBUtil.class);
    PowerMockito.mockStatic(HttpUtil.class);
    PowerMockito.mockStatic(LoggerFactory.class);
    when(LoggerFactory.getLogger(any(Class.class))).thenAnswer(ignore -> PowerMockito.mock(Logger.class));
    
    final var pokemonFromNetwork = List.of("pokemon1", "pokemon2", "pokemon3");
    when(HttpUtil.fetchAllPokemon(anyInt(), anyInt())).thenAnswer(ignore -> pokemonFromNetwork);
    
    when(DBUtil.queryPokemonPowers(pokemonFromNetwork)).thenAnswer(ignore -> Map.of("pokemon1", "power1", "pokemon3", "power3"));
    when(HttpUtil.fetchPokemonPower("pokemon2")).thenAnswer(ignore -> "power2");
    PokemonDemo.play(AppKt.POKEMON_OFFSET_TO_FETCH, AppKt.POKEMON_LIMIT_TO_FETCH);
    PowerMockito.verifyStatic(DBUtil.class);
    DBUtil.batchInsertPokemonPowers(List.of(new Pair<>("pokemon2", "power2")));
  }
}

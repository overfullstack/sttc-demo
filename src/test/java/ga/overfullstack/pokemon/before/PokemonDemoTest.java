package ga.overfullstack.pokemon.before;

import kotlin.Pair;
import org.jetbrains.exposed.sql.Database;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

class PokemonDemoTest {
  @Test
  void play() {
    try (final var httpUtil = Mockito.mockStatic(HttpUtil.class);
        final var dbUtil = Mockito.mockStatic(DBUtil.class);
        final var database = Mockito.mockStatic(Database.class)) {
      database.when(() -> Database.Companion.connect(any(), any(), any())).thenReturn(null);
      final var pokemonFromNetwork = List.of("pokemon1", "pokemon2", "pokemon3");
      httpUtil.when(() -> HttpUtil.fetchAllPokemon(anyInt(), anyInt())).thenReturn(pokemonFromNetwork);
      dbUtil.when(() -> DBUtil.queryPokemonPowers(pokemonFromNetwork))
          .thenReturn(Map.of("pokemon1", "power1", "pokemon3", "power3"));
      httpUtil.when(() -> HttpUtil.fetchPokemonPower("pokemon2")).thenReturn("power2");
      PokemonDemo.play();
      dbUtil.verify(() -> DBUtil.batchInsertPokemonPowers(List.of(new Pair<>("pokemon2", "power2"))));
    }
  }
}

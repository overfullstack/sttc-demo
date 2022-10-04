package ga.overfullstack.pokemon.after;

import static ga.overfullstack.pokemon.before.App.POKEMON_LIMIT_TO_FETCH;
import static ga.overfullstack.pokemon.before.App.POKEMON_OFFSET_TO_FETCH;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import kotlin.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

class PokemonCollectorTest {
  @Test
  @DisplayName("Collect Pokemon")
  void collectPokemon() {
    final var pokemonHttpFake =
        new PokemonHttp() {
          @Override
          public List<String> fetchAllPokemon(int offset, int limit) {
            return List.of("pokemon1", "pokemon2", "pokemon3");
          }

          @Override
          public String fetchPokemonPower(String pokemonName) {
            if ("pokemon2".equals(pokemonName)) {
              return "power2";
            }
            throw new IllegalArgumentException("Unknown pokemon");
          }
        };
    final var pokemonDbFake =
        new PokemonDao() {
          @Override
          public Map<String, String> queryPokemonPowers(List<String> pokemonNames) {
            return Map.of(
                "pokemon1", "power1",
                "pokemon3", "power3");
          }

          @Override
          public void batchInsertPokemonPowers(List<Pair<String, String>> pokemonToPower) {
            assertEquals(List.of(new Pair<>("pokemon2", "power2")), pokemonToPower);
          }

          @Override
          public Map<String, String> queryAllPokemonPowers() {
            return Map.of(
                "pokemon1", "power1",
                "pokemon2", "power2",
                "pokemon3", "power3");
          }
        };
    new PokemonCollector(pokemonDbFake, pokemonHttpFake, Mockito.mock(Logger.class))
        .play(POKEMON_OFFSET_TO_FETCH, POKEMON_LIMIT_TO_FETCH);
  }
}

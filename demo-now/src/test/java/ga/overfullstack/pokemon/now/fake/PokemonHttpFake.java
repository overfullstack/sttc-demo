package ga.overfullstack.pokemon.now.fake;

import static ga.overfullstack.pokemon.now.fake.TestConstants.POKEMON_FIELD_TYPE_INFO;
import static ga.overfullstack.pokemon.now.fake.TestConstants.POKEMON_HTTP_FAKE;
import static ga.overfullstack.pokemon.now.fake.TestConstants.tableToPokemonMap;

import ga.overfullstack.loki.dud.Dud;
import ga.overfullstack.pokemon.now.PokemonHttp;
import java.util.List;
import java.util.Map;
import kotlin.collections.MapsKt;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * This is an example for a module-specific Fake. This makes use of the Dud to generate Fake data.
 */
@Component(POKEMON_HTTP_FAKE)
public class PokemonHttpFake implements PokemonHttp {
  public static final String HTTP_RESPONSE_PRE_EXISTING_POKEMON_FAKE_KEY =
      "HttpResponseExistingPokemonFakeKey";
  public static final String HTTP_RESPONSE_NEW_POKEMON_FAKE_KEY = "HttpResponseNewPokemonFakeKey";

  @Override
  public List<String> fetchAllPokemonNames(int ignore1, int ignore2) {
    return generateFakePokemonMixOfPreExistingAndNew().keySet().stream().toList();
  }
  
  @NotNull
  private static Map<String, String> generateFakePokemonMixOfPreExistingAndNew() {
    return MapsKt.plus(
        tableToPokemonMap(Dud.getOrGenerateTableIfAbsent(HTTP_RESPONSE_PRE_EXISTING_POKEMON_FAKE_KEY, 1,
            POKEMON_FIELD_TYPE_INFO, null)),
        tableToPokemonMap(Dud.getOrGenerateTableIfAbsent(HTTP_RESPONSE_NEW_POKEMON_FAKE_KEY, 2,
            POKEMON_FIELD_TYPE_INFO, null)));
  }

  @Override
  public String fetchPokemonPower(String pokemonName) {
    return tableToPokemonMap(Dud.getOrGenerateTableIfAbsent(HTTP_RESPONSE_NEW_POKEMON_FAKE_KEY, null, null, null)).get(pokemonName);
  }
}

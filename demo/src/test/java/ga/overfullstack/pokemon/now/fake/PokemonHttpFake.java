package ga.overfullstack.pokemon.now.fake;

import static ga.overfullstack.pokemon.now.fake.config.BeanName.POKEMON_HTTP_FAKE;

import ga.overfullstack.loki.dud.AnyToAny;
import ga.overfullstack.pokemon.now.PokemonHttp;
import java.util.List;
import java.util.Map;
import kotlin.collections.MapsKt;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * This is an example for a module-specific Fake. This makes use of the Dud layer to generate Fake
 * data.
 */
@Component(POKEMON_HTTP_FAKE)
public class PokemonHttpFake implements PokemonHttp {
  public static final String HTTP_RESPONSE_EXISTING_POKEMON_FAKE_KEY =
      "HttpResponseExistingPokemonFakeKey";
  public static final String HTTP_RESPONSE_NEW_POKEMON_FAKE_KEY = "HttpResponseNewPokemonFakeKey";

  @Override
  public List<String> fetchAllPokemon(int ignore1, int ignore2) {
    return generateFakePokemonResponse().keySet().stream().toList();
  }

  @NotNull
  private static Map<String, String> generateFakePokemonResponse() {
    return MapsKt.plus(
        AnyToAny.getMap(HTTP_RESPONSE_EXISTING_POKEMON_FAKE_KEY, String.class, String.class, 1),
        AnyToAny.getMap(HTTP_RESPONSE_NEW_POKEMON_FAKE_KEY, String.class, String.class, 2));
  }

  @Override
  public String fetchPokemonPower(String pokemonName) {
    return AnyToAny.<String, String>getMap(HTTP_RESPONSE_NEW_POKEMON_FAKE_KEY).get(pokemonName);
  }
}

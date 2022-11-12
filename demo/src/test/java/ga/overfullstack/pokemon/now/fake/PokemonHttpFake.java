package ga.overfullstack.pokemon.now.fake;

import static ga.overfullstack.pokemon.now.fake.config.BeanName.POKEMON_HTTP_FAKE;

import ga.overfullstack.loki.dud.AnyToAny;
import ga.overfullstack.pokemon.now.PokemonHttp;
import java.util.List;
import org.springframework.stereotype.Component;

/** This is an example for a module-specific Fake. */
@Component(POKEMON_HTTP_FAKE)
public class PokemonHttpFake implements PokemonHttp {
  public static final String FAKE_RESPONSE_KEY = "FakeResponseKey";

  @Override
  public List<String> fetchAllPokemon(int ignore1, int ignore2) {
    return AnyToAny.getMap(FAKE_RESPONSE_KEY, String.class, String.class, 3).keySet().stream()
        .toList();
  }

  @Override
  public String fetchPokemonPower(String pokemonName) {
    return AnyToAny.<String, String>getMap(FAKE_RESPONSE_KEY).get(pokemonName);
  }
}

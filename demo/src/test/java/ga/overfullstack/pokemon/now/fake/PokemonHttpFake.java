package ga.overfullstack.pokemon.now.fake;

import static ga.overfullstack.pokemon.now.fake.config.BeanName.POKEMON_HTTP_FAKE;

import ga.overfullstack.pokemon.now.PokemonHttp;
import java.util.List;
import org.springframework.stereotype.Component;

/** This is an example for a module-specific Fake. */
@Component(POKEMON_HTTP_FAKE)
public class PokemonHttpFake implements PokemonHttp {
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
}

package ga.overfullstack.pokemon.now;

import ga.overfullstack.legacy.HttpUtil;
import java.util.List;
import org.springframework.stereotype.Component;

/** This is an example for a module-specific Port & Adapter. */
@Component
public interface PokemonHttp {
  default List<String> fetchAllPokemon(int offset, int limit) {
    return HttpUtil.fetchAllPokemon(offset, limit);
  }

  default String fetchPokemonPower(String pokemonName) {
    return HttpUtil.fetchPokemonPower(pokemonName);
  }
}

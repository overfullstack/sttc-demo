package ga.overfullstack.pokemon.now;

import ga.overfullstack.legacy.HttpUtil;
import java.util.List;

/** This is an example for a module-specific Port & Adapter. */
public interface PokemonHttp {
  default List<String> fetchAllPokemonNames(int offset, int limit) {
    return HttpUtil.fetchAllPokemonNames(offset, limit);
  }

  default String fetchPokemonPower(String pokemonName) {
    return HttpUtil.fetchPokemonPower(pokemonName);
  }
}

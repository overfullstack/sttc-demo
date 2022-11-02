package ga.overfullstack.pokemon.now;

import ga.overfullstack.legacy.HttpUtil;
import java.util.List;

/** This is an example for module specific Port & Adapter. */
interface PokemonHttp {
  default List<String> fetchAllPokemon(int offset, int limit) {
    return HttpUtil.fetchAllPokemon(offset, limit);
  }

  default String fetchPokemonPower(String pokemonName) {
    return HttpUtil.fetchPokemonPower(pokemonName);
  }
}

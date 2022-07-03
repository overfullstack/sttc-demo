package ga.overfullstack.pokemon.after;

import ga.overfullstack.pokemon.HttpUtil;
import java.util.List;

interface PokemonHttp {
  default List<String> fetchAllPokemon(int offset, int limit) {
    return HttpUtil.fetchAllPokemon(offset, limit);
  }
  default String fetchPokemonPower(String pokemonName) {
    return HttpUtil.fetchPokemonPower(pokemonName);
  }
}

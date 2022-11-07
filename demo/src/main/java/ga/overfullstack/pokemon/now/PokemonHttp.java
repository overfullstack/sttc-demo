package ga.overfullstack.pokemon.now;

import ga.overfullstack.legacy.HttpUtil;
import org.springframework.stereotype.Component;
import java.util.List;

import static ga.overfullstack.pokemon.now.config.BeanName.POKEMON_HTTP;

/** This is an example for a module specific Port & Adapter. */
@Component(POKEMON_HTTP)
public interface PokemonHttp {
  default List<String> fetchAllPokemon(int offset, int limit) {
    return HttpUtil.fetchAllPokemon(offset, limit);
  }

  default String fetchPokemonPower(String pokemonName) {
    return HttpUtil.fetchPokemonPower(pokemonName);
  }
}

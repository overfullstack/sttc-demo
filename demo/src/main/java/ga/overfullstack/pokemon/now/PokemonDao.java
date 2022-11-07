package ga.overfullstack.pokemon.now;

import ga.overfullstack.legacy.DBUtil;
import java.util.List;
import java.util.Map;
import kotlin.Pair;
import org.springframework.stereotype.Component;

/** This is an example for a module-specific Port & Adapter. */
@Component
public interface PokemonDao {
  default Map<String, String> queryPokemonPowers(List<String> pokemonNames) {
    return DBUtil.queryPokemonPowers(pokemonNames);
  }

  default void batchInsertPokemonPowers(List<Pair<String, String>> pokemonToPower) {
    DBUtil.batchInsertPokemonPowers(pokemonToPower);
  }

  default Map<String, String> queryAllPokemonPowers() {
    return DBUtil.queryAllPokemonPowers();
  }
}

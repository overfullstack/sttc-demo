package ga.overfullstack.pokemon.after;

import ga.overfullstack.pokemon.DBUtil;
import kotlin.Pair;
import java.util.List;
import java.util.Map;

interface PokemonDao {
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

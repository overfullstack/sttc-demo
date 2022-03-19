package ga.overfullstack.pokemon.before;

import kotlin.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PokemonDemo {
  private static final Logger logger = LoggerFactory.getLogger(PokemonDemo.class);
  private static final int POKEMON_LIMIT_TO_FETCH = 5;

  public static void main(String[] args) {
    serve();
  }

  static void serve() {
    // Fetch all Pokémon
    final var pokemonNames = HttpUtil.fetchAllPokemon(POKEMON_LIMIT_TO_FETCH);
    logger.info("Pokémon fetched: {}", pokemonNames);

    // Find DB match for fetched Pokémon.
    final var pokemonNameToPower = DBUtil.queryPokemonPowers(pokemonNames);
    logger.info("{} Matching Pokémon with Powers in DB: {}", pokemonNameToPower.size(), pokemonNameToPower);

    // Fetch powers for missing Pokémon.
    final var missingPokemonNames = pokemonNames.stream()
        .filter(key -> !pokemonNameToPower.containsKey(key)).toList();
    logger.info("Fetch for {} missing Pokémon: {}", missingPokemonNames.size(), missingPokemonNames);

    // Insert new fetched Pokémon into the DB.
    final var newPokemonToInsert = missingPokemonNames.stream()
        .map(pokemonName -> new Pair<>(pokemonName, HttpUtil.fetchPokemonPower(pokemonName))).toList();
    DBUtil.batchInsertPokemonPowers(newPokemonToInsert);
    final var allPokemonWithPowers = DBUtil.queryAllPokemonPowers();
    logger.info("{} Pokémon with Powers in DB: {}", allPokemonWithPowers.size(), allPokemonWithPowers);
  }

}

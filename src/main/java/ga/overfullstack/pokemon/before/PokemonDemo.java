package ga.overfullstack.pokemon.before;

import kotlin.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;

public class PokemonDemo {
  private static final Logger logger = LoggerFactory.getLogger(PokemonDemo.class);
  private static final Random random = new Random();
  private static final int POKEMON_OFFSET_TO_FETCH = random.ints(1, 100).findFirst().orElse(1);
  private static final int POKEMON_LIMIT_TO_FETCH = random.ints(1, 6).findFirst().orElse(1);

  public static void main(String[] args) {
    play();
  }

  static void play() {
    // Fetch all Pokémon
    final var pokemonNames = HttpUtil.fetchAllPokemon(POKEMON_OFFSET_TO_FETCH, POKEMON_LIMIT_TO_FETCH);
    logger.info("Pokémon fetched: {}", pokemonNames);

    // Find DB match for fetched Pokémon.
    final var existingPokemonNameToPower = DBUtil.queryPokemonPowers(pokemonNames);
    logger.info("{} Matching Pokémon with Powers in DB: {}", existingPokemonNameToPower.size(), existingPokemonNameToPower);

    // Fetch powers for missing Pokémon.
    final var missingPokemonNames = pokemonNames.stream()
        .filter(key -> !existingPokemonNameToPower.containsKey(key)).toList();
    logger.info("Fetch for {} missing Pokémon: {}", missingPokemonNames.size(), missingPokemonNames);
    final var newPokemonToInsert = missingPokemonNames.stream()
        .map(pokemonName -> new Pair<>(pokemonName, HttpUtil.fetchPokemonPower(pokemonName))).toList();
    
    // Insert new fetched Pokémon into the DB.
    DBUtil.batchInsertPokemonPowers(newPokemonToInsert);
    
    // Fetch all Pokémon in DB.
    final var allPokemonWithPowers = DBUtil.queryAllPokemonPowers();
    logger.info("{} Pokémon with Powers in DB: {}", allPokemonWithPowers.size(), allPokemonWithPowers);
  }

}

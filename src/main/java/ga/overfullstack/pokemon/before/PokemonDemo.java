package ga.overfullstack.pokemon.before;

import java.util.Map;
import java.util.Random;
import kotlin.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ga.overfullstack.pokemon.before.AppKt.POKEMON_LIMIT_TO_FETCH;
import static ga.overfullstack.pokemon.before.AppKt.POKEMON_OFFSET_TO_FETCH;

public class PokemonDemo {
  private static final Logger logger = LoggerFactory.getLogger(PokemonDemo.class);

  private PokemonDemo() {
  }

  public static void main(String[] args) {
    play(POKEMON_OFFSET_TO_FETCH, POKEMON_LIMIT_TO_FETCH);
  }

  static Map<String, String> play(int pokemonOffsetToFetch, int pokemonLimitToFetch) {
    validate(pokemonOffsetToFetch, pokemonLimitToFetch);
    
    // Fetch all Pokémon
    final var pokemonNames = HttpUtil.fetchAllPokemon(pokemonOffsetToFetch, pokemonLimitToFetch);
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
    return allPokemonWithPowers;
  }
  
  static void validate(int pokemonOffsetToFetch, int pokemonLimitToFetch) {
    if (pokemonOffsetToFetch < 0 || pokemonLimitToFetch < 0 || pokemonLimitToFetch > 10) {
      throw new IllegalArgumentException("Invalid offset or limit : offset=" + pokemonOffsetToFetch + ", limit=" + pokemonLimitToFetch);
    }
  }

}

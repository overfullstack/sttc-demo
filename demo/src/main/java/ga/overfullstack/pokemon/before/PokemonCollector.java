package ga.overfullstack.pokemon.before;

import ga.overfullstack.legacy.DBUtil;
import ga.overfullstack.legacy.HttpUtil;
import ga.overfullstack.legacy.LoadFromDBException;
import ga.overfullstack.pokemon.Pokemon;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PokemonCollector {
  private static final Logger logger = LoggerFactory.getLogger(PokemonCollector.class);

  private PokemonCollector() {}

  /** POP (Procedure Oriented Programming) */
  public static Map<String, String> play(int pokemonOffsetToFetch, int pokemonLimitToFetch) {
    validate(pokemonOffsetToFetch, pokemonLimitToFetch);

    // Fetch all Pokémon names
    final var fetchedPokemonNames =
        HttpUtil.fetchAllPokemonNames(pokemonOffsetToFetch, pokemonLimitToFetch);
    logger.info("Pokémon fetched: {}", fetchedPokemonNames);

    if (fetchedPokemonNames.isEmpty()) {
      return DBUtil.queryAllPokemonWithPowers();
    }

    // Find DB match for fetched Pokémon.
    final var existingPokemonNameToPower = DBUtil.queryPokemonPowers(fetchedPokemonNames);
    logger.info(
        "{} Matching Pokémon with Powers in DB: {}",
        existingPokemonNameToPower.size(),
        existingPokemonNameToPower);

    // Find missing Pokémon from DB (fetchedPokemonNames - existingPokemonNames).
    final var missingPokemonNames =
        fetchedPokemonNames.stream()
            .filter(key -> !existingPokemonNameToPower.containsKey(key))
            .toList();
    logger.info(
        "Fetch for {} missing Pokémon: {}", missingPokemonNames.size(), missingPokemonNames);

    // Fetch powers for missing Pokémon.
    final var newPokemonToInsert =
        missingPokemonNames.stream()
            .map(pokemonName -> new Pokemon(pokemonName, HttpUtil.fetchPokemonPower(pokemonName)))
            .toList();

    // Insert new fetched Pokémon into the DB.
    for (final var pokemon : newPokemonToInsert) {
      try {
        BeanToEntityMapper.insertInDB(pokemon);
      } catch (LoadFromDBException e) {
        logger.error("Insertion failed for Pokémon: {}, with exception: {}", pokemon, e);
      }
    }

    // Fetch all collected Pokémon in DB.
    final var allPokemonWithPowers = DBUtil.queryAllPokemonWithPowers();
    logger.info(
        "{} Pokémon with Powers in DB: {}", allPokemonWithPowers.size(), allPokemonWithPowers);
    return allPokemonWithPowers;
  }

  static void validate(int pokemonOffsetToFetch, int pokemonLimitToFetch) {
    if (pokemonOffsetToFetch < 0 || pokemonLimitToFetch < 0 || pokemonLimitToFetch > 10) {
      throw new IllegalArgumentException(
          "Invalid offset or limit : offset="
              + pokemonOffsetToFetch
              + ", limit="
              + pokemonLimitToFetch);
    }
  }

  // -- Quick play --
  public static void main(String[] args) {
    play(App.POKEMON_OFFSET_TO_FETCH, App.POKEMON_LIMIT_TO_FETCH);
  }
}

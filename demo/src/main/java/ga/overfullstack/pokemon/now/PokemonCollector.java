package ga.overfullstack.pokemon.now;

import java.util.Map;
import ga.overfullstack.loki.LoggerSupplier;
import kotlin.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PokemonCollector {
  private final PokemonDao pokemonDao;
  private final PokemonHttp pokemonHttp;
  private final Logger logger;

  public static void main(String[] args) {
    final var ctx = new AnnotationConfigApplicationContext(PokemonCollector.class);
    final var pokemonCollector = ctx.getBean(PokemonCollector.class);
    pokemonCollector.play(App.POKEMON_OFFSET_TO_FETCH, App.POKEMON_LIMIT_TO_FETCH);
  }

  @Autowired
  public PokemonCollector(PokemonDao pokemonDao, PokemonHttp pokemonHttp, LoggerSupplier loggerSupplier) {
    this.pokemonDao = pokemonDao;
    this.pokemonHttp = pokemonHttp;
    this.logger = loggerSupplier.supply(this.getClass());
  }

  public Map<String, String> play(int pokemonOffsetToFetch, int pokemonLimitToFetch) {
    validate(pokemonOffsetToFetch, pokemonLimitToFetch);

    // Fetch all Pokémon
    final var pokemonNames = pokemonHttp.fetchAllPokemon(pokemonOffsetToFetch, pokemonLimitToFetch);
    logger.info("Pokémon fetched: {}", pokemonNames);

    if (pokemonNames.isEmpty()) {
      return Map.of();
    }

    // Find DB match for fetched Pokémon.
    final var existingPokemonNameToPower = pokemonDao.queryPokemonPowers(pokemonNames);
    logger.info(
        "{} Matching Pokémon with Powers in DB: {}",
        existingPokemonNameToPower.size(),
        existingPokemonNameToPower);

    // Fetch powers for missing Pokémon.
    final var missingPokemonNames =
        pokemonNames.stream().filter(key -> !existingPokemonNameToPower.containsKey(key)).toList();
    logger.info(
        "Fetch for {} missing Pokémon: {}", missingPokemonNames.size(), missingPokemonNames);
    final var newPokemonToInsert =
        missingPokemonNames.stream()
            .map(pokemonName -> new Pair<>(pokemonName, pokemonHttp.fetchPokemonPower(pokemonName)))
            .toList();

    // Insert new fetched Pokémon into the DB.
    pokemonDao.batchInsertPokemonPowers(newPokemonToInsert);

    // Fetch all collected Pokémon in DB.
    final var allPokemonWithPowers = pokemonDao.queryAllPokemonPowers();
    logger.info(
        "{} Pokémon with Powers in DB: {}", allPokemonWithPowers.size(), allPokemonWithPowers);
    return allPokemonWithPowers;
  }

  /**
   * No side-effects, so no need to create Validator class for this function
   *
   * @param pokemonOffsetToFetch
   * @param pokemonLimitToFetch
   */
  static void validate(int pokemonOffsetToFetch, int pokemonLimitToFetch) {
    if (pokemonOffsetToFetch < 0 || pokemonLimitToFetch < 0 || pokemonLimitToFetch > 10) {
      throw new IllegalArgumentException(
          "Invalid offset or limit : offset="
              + pokemonOffsetToFetch
              + ", limit="
              + pokemonLimitToFetch);
    }
  }
}

package ga.overfullstack.pokemon.now;

import ga.overfullstack.legacy.LoadFromDBException;
import ga.overfullstack.loki.LoggerSupplier;
import ga.overfullstack.pokemon.Pokemon;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PokemonCollector {
  private final PokemonDao pokemonDao;
  private final PokemonHttp pokemonHttp;
  private final BeanToEntity beanToEntity;
  private final Logger logger;

  public static void main(String[] args) throws LoadFromDBException {
    final var ctx = new AnnotationConfigApplicationContext(PokemonCollector.class);
    final var pokemonCollector = ctx.getBean(PokemonCollector.class);
    pokemonCollector.play(App.POKEMON_OFFSET_TO_FETCH, App.POKEMON_LIMIT_TO_FETCH);
  }

  @Autowired
  public PokemonCollector(
      PokemonDao pokemonDao,
      PokemonHttp pokemonHttp,
      BeanToEntity beanToEntity,
      LoggerSupplier loggerSupplier) {
    this.pokemonDao = pokemonDao;
    this.pokemonHttp = pokemonHttp;
    this.beanToEntity = beanToEntity;
    this.logger = loggerSupplier.supply(this.getClass());
  }

  public Map<String, String> play(int pokemonOffsetToFetch, int pokemonLimitToFetch)
      throws LoadFromDBException {
    validate(pokemonOffsetToFetch, pokemonLimitToFetch);

    // Fetch all Pokémon
    final var fetchedPokemonNames =
        pokemonHttp.fetchAllPokemon(pokemonOffsetToFetch, pokemonLimitToFetch);
    logger.info("Pokémon fetched: {}", fetchedPokemonNames);

    if (fetchedPokemonNames.isEmpty()) {
      return Map.of();
    }

    // Find DB match for fetched Pokémon.
    final var existingPokemonNameToPower = pokemonDao.queryPokemonPowers(fetchedPokemonNames);
    logger.info(
        "{} Matching Pokémon with Powers in DB: {}",
        existingPokemonNameToPower.size(),
        existingPokemonNameToPower);

    // Find missing Pokémon from DB (fetchedPokemonNames - existingPokemonNames).
    final var pokemonNamesMissingFromDB =
        fetchedPokemonNames.stream()
            .filter(key -> !existingPokemonNameToPower.containsKey(key))
            .toList();
    logger.info(
        "Fetch powers for {} missing Pokémon: {}",
        pokemonNamesMissingFromDB.size(),
        pokemonNamesMissingFromDB);

    // Fetch powers for missing Pokémon.
    final var newPokemonToInsert =
        pokemonNamesMissingFromDB.stream()
            .map(
                pokemonName -> new Pokemon(pokemonName, pokemonHttp.fetchPokemonPower(pokemonName)))
            .toList();

    // Insert new fetched Pokémon into the DB.
    for (final var poke : newPokemonToInsert) {
      beanToEntity.insertInDB(poke);
    }

    // Fetch all collected Pokémon in DB.
    final var allPokemonWithPowers = pokemonDao.queryAllPokemonPowers();
    logger.info(
        "{} Pokémon with Powers in DB: {}", allPokemonWithPowers.size(), allPokemonWithPowers);
    return allPokemonWithPowers;
  }

  /**
   * No side-effects, so no need to create a Validator class for this function
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

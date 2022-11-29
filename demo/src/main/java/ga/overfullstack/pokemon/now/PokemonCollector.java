package ga.overfullstack.pokemon.now;

import static ga.overfullstack.loki.BeanName.LOGGER_SUPPLIER_LOKI;

import ga.overfullstack.legacy.LoadFromDBException;
import ga.overfullstack.loki.adapter.LoggerSupplier;
import ga.overfullstack.pokemon.Pokemon;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PokemonCollector {
  private final PokemonDao pokemonDao;
  private final PokemonHttp pokemonHttp;
  private final BeanToEntityMapper beanToEntityMapper;
  private final Logger logger;

  @Autowired
  public PokemonCollector(
      PokemonDao pokemonDao,
      PokemonHttp pokemonHttp,
      BeanToEntityMapper beanToEntityMapper,
      @Qualifier(LOGGER_SUPPLIER_LOKI) LoggerSupplier loggerSupplier) {
    this.pokemonDao = pokemonDao;
    this.pokemonHttp = pokemonHttp;
    this.beanToEntityMapper = beanToEntityMapper;
    this.logger = loggerSupplier.supply(this.getClass());
  }

  public Map<String, String> play(int pokemonOffsetToFetch, int pokemonLimitToFetch) {
    validate(pokemonOffsetToFetch, pokemonLimitToFetch);

    // Fetch all Pokémon
    final var fetchedPokemonNames =
        pokemonHttp.fetchAllPokemon(pokemonOffsetToFetch, pokemonLimitToFetch);
    logger.info("Pokémon fetched: {}", fetchedPokemonNames);

    if (fetchedPokemonNames.isEmpty()) {
      return pokemonDao.queryAllPokemonPowers();
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
    for (final var pokemon : newPokemonToInsert) {
      try {
        beanToEntityMapper.insertInDB(pokemon);
      } catch (LoadFromDBException e) {
        logger.error("Insertion failed for Pokémon: {}, with exception: {}", pokemon, e);
      }
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

  // -- Quick Play --
  public static void main(String[] args) {
    final var ctx = new AnnotationConfigApplicationContext(PokemonCollector.class);
    final var pokemonCollector = ctx.getBean(PokemonCollector.class);
    pokemonCollector.play(App.POKEMON_OFFSET_TO_FETCH, App.POKEMON_LIMIT_TO_FETCH);
  }
}

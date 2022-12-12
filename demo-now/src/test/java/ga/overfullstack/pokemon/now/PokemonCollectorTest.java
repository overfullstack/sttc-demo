package ga.overfullstack.pokemon.now;

import static ga.overfullstack.loki.fake.BeanName.ENTITY_ACCESSOR_LOKI_FAKE;
import static ga.overfullstack.loki.fake.BeanName.LOGGER_NO_OP_SUPPLIER_LOKI;
import static ga.overfullstack.pokemon.now.PokemonCollector.POKEMON_LIMIT_TO_FETCH;
import static ga.overfullstack.pokemon.now.PokemonCollector.POKEMON_OFFSET_TO_FETCH;
import static ga.overfullstack.pokemon.now.fake.PokemonDaoFake.INIT_DB_RECORDS_FAKE_KEY;
import static ga.overfullstack.pokemon.now.fake.PokemonHttpFake.HTTP_RESPONSE_NEW_POKEMON_FAKE_KEY;
import static ga.overfullstack.pokemon.now.fake.config.BeanName.POKEMON_DAO_FAKE;
import static ga.overfullstack.pokemon.now.fake.config.BeanName.POKEMON_HTTP_FAKE;
import static org.assertj.core.api.Assertions.assertThat;

import ga.overfullstack.legacy.Entity;
import ga.overfullstack.legacy.LoadFromDBException;
import ga.overfullstack.loki.adapter.EntityAccessor;
import ga.overfullstack.loki.adapter.LoggerSupplier;
import ga.overfullstack.loki.dud.AnyToAny;
import ga.overfullstack.loki.dud.Dud;
import ga.overfullstack.loki.fake.adapter.LokiConfigForTest;
import ga.overfullstack.pokemon.now.fake.PokemonDaoFake;
import ga.overfullstack.pokemon.now.fake.PokemonHttpFake;
import java.util.Collections;
import java.util.List;
import kotlin.collections.MapsKt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {LokiConfigForTest.class, PokemonHttpFake.class, PokemonDaoFake.class})
class PokemonCollectorTest {

  @Autowired
  @Qualifier(LOGGER_NO_OP_SUPPLIER_LOKI)
  LoggerSupplier loggerNoOpSupplier;

  @Autowired
  @Qualifier(POKEMON_HTTP_FAKE)
  PokemonHttp pokemonHttpFake;

  @Autowired
  @Qualifier(POKEMON_DAO_FAKE)
  PokemonDao pokemonDaoFake;

  @Autowired
  @Qualifier(ENTITY_ACCESSOR_LOKI_FAKE)
  EntityAccessor entityAccessorFake;

  BeanToEntityMapper beanToEntityMapperFake;

  @AfterEach
  void afterEach() {
    Dud.clear(); // Important to clear Dud after each test to keep tests independent
  }

  @BeforeEach
  void setUp() {
    beanToEntityMapperFake = new BeanToEntityMapper(entityAccessorFake, loggerNoOpSupplier);
  }

  @Test
  @DisplayName("Collect Pokemon")
  void collectPokemon() {
    final var result =
        new PokemonCollector(
                pokemonDaoFake, pokemonHttpFake, beanToEntityMapperFake, loggerNoOpSupplier)
            .play(POKEMON_OFFSET_TO_FETCH, POKEMON_LIMIT_TO_FETCH);
    // No need to assert on internal details like Method interactions, as we can precisely assert
    // data inserted via
    // EntityAccessor
    assertThat(PokemonDaoFake.getDataInsertedViaEntityAccessor())
        .containsExactlyInAnyOrderEntriesOf(AnyToAny.getMap(HTTP_RESPONSE_NEW_POKEMON_FAKE_KEY));
    final var expectedResult = // Init DB records + New Pokemon from response
        MapsKt.plus(
            AnyToAny.<String, String>getMap(INIT_DB_RECORDS_FAKE_KEY),
            AnyToAny.getMap(HTTP_RESPONSE_NEW_POKEMON_FAKE_KEY));
    assertThat(result).containsExactlyInAnyOrderEntriesOf(expectedResult);
  }

  @Test
  @DisplayName("Empty Http Response should not add new Pokémon")
  void emptyHttpResponseShouldNotAddNewPokemon() {
    final var pokemonHttpFakeWithEmptyResponse =
        new PokemonHttpFake() {
          @Override
          public List<String> fetchAllPokemon(int ignore1, int ignore2) {
            return Collections.emptyList();
          }
        };
    final var result =
        new PokemonCollector(
                pokemonDaoFake,
                pokemonHttpFakeWithEmptyResponse,
                beanToEntityMapperFake,
                loggerNoOpSupplier)
            .play(POKEMON_OFFSET_TO_FETCH, POKEMON_LIMIT_TO_FETCH);
    assertThat(result).containsExactlyInAnyOrderEntriesOf(pokemonDaoFake.queryAllPokemonPowers());
  }

  @Test
  @DisplayName("load from DB throws, no new pokemon inserted into DB")
  void loadFromDbThrows() {
    final var entityAccessorThrowsOnLoadFromDb =
        new EntityAccessor() {
          @Override
          public <T extends Entity> T loadNew(Class<T> type) throws LoadFromDBException {
            throw new LoadFromDBException("Fake Exception");
          }
        };
    final var result =
        new PokemonCollector(
                pokemonDaoFake,
                pokemonHttpFake,
                new BeanToEntityMapper(entityAccessorThrowsOnLoadFromDb, loggerNoOpSupplier),
                loggerNoOpSupplier)
            .play(POKEMON_OFFSET_TO_FETCH, POKEMON_LIMIT_TO_FETCH);
    assertThat(result).containsExactlyInAnyOrderEntriesOf(pokemonDaoFake.queryAllPokemonPowers());
  }
}

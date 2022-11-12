package ga.overfullstack.pokemon.now;

import static ga.overfullstack.loki.fake.BeanName.LOGGER_NO_OP_SUPPLIER_LOKI;
import static ga.overfullstack.pokemon.before.App.POKEMON_LIMIT_TO_FETCH;
import static ga.overfullstack.pokemon.before.App.POKEMON_OFFSET_TO_FETCH;
import static ga.overfullstack.pokemon.now.fake.PokemonDaoFake.FAKE_DB_RECORDS_KEY;
import static ga.overfullstack.pokemon.now.fake.PokemonHttpFake.FAKE_RESPONSE_KEY;
import static ga.overfullstack.pokemon.now.fake.config.BeanName.POKEMON_DAO_FAKE;
import static ga.overfullstack.pokemon.now.fake.config.BeanName.POKEMON_HTTP_FAKE;

import ga.overfullstack.legacy.LoadFromDBException;
import ga.overfullstack.loki.LoggerSupplier;
import ga.overfullstack.loki.dud.AnyToAny;
import ga.overfullstack.loki.fake.LokiConfig;
import ga.overfullstack.pokemon.now.fake.config.ConfigForTest;
import kotlin.collections.MapsKt;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LokiConfig.class, ConfigForTest.class, BeanToEntity.class})
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

  // Fakes are injected transitively into this
  @Autowired BeanToEntity beanToEntity;

  @Test
  @DisplayName("Collect Pokemon")
  @ExtendWith(SpringExtension.class)
  void collectPokemon() throws LoadFromDBException {
    final var result =
        new PokemonCollector(pokemonDaoFake, pokemonHttpFake, beanToEntity, loggerNoOpSupplier)
            .play(POKEMON_OFFSET_TO_FETCH, POKEMON_LIMIT_TO_FETCH);
    final var expectedResult =
        MapsKt.plus(
            AnyToAny.<String, String>getMap(FAKE_RESPONSE_KEY),
            AnyToAny.getMap(FAKE_DB_RECORDS_KEY));
    Assertions.assertThat(result).containsExactlyInAnyOrderEntriesOf(expectedResult);
  }
}

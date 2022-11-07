package ga.overfullstack.pokemon.now;

import ga.overfullstack.loki.LoggerSupplier;
import ga.overfullstack.loki.fake.CoreFakeConfig;
import ga.overfullstack.pokemon.now.fake.PokemonDAOFake;
import ga.overfullstack.pokemon.now.fake.config.ConfigForTest;
import kotlin.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static ga.overfullstack.loki.fake.BeanName.LOGGER_NO_OP_SUPPLIER_LOKI;
import static ga.overfullstack.pokemon.before.App.POKEMON_LIMIT_TO_FETCH;
import static ga.overfullstack.pokemon.before.App.POKEMON_OFFSET_TO_FETCH;
import static ga.overfullstack.pokemon.now.fake.config.BeanName.POKEMON_HTTP_FAKE;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CoreFakeConfig.class, ConfigForTest.class})
class PokemonCollectorTest {

  @Autowired
  @Qualifier(LOGGER_NO_OP_SUPPLIER_LOKI)
  LoggerSupplier loggerNoOpSupplier;

  @Autowired
  @Qualifier(POKEMON_HTTP_FAKE)
  PokemonHttp pokemonHttpFake;

  @Test
  @DisplayName("Collect Pokemon")
  @ExtendWith(SpringExtension.class)
  void collectPokemon() {
    final var pokemonDbFake =
        new PokemonDAOFake() {
          @Override
          public void batchInsertPokemonPowers(List<Pair<String, String>> pokemonToPower) {
            Assertions.assertEquals(List.of(new Pair<>("pokemon2", "power2")), pokemonToPower);
          }
        };
    new PokemonCollector(pokemonDbFake, pokemonHttpFake, loggerNoOpSupplier)
        .play(POKEMON_OFFSET_TO_FETCH, POKEMON_LIMIT_TO_FETCH);
  }
}

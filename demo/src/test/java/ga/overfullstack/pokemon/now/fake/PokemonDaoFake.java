package ga.overfullstack.pokemon.now.fake;

import static ga.overfullstack.pokemon.now.fake.PokemonHttpFake.HTTP_RESPONSE_EXISTING_POKEMON_FAKE_KEY;
import static ga.overfullstack.pokemon.now.fake.config.BeanName.POKEMON_DAO_FAKE;

import ga.overfullstack.loki.dud.AnyToAny;
import ga.overfullstack.loki.dud.MultiAnyToAny;
import ga.overfullstack.pokemon.now.PokemonDao;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

/** This is an example for a module-specific Fake. */
@Component(POKEMON_DAO_FAKE)
public class PokemonDaoFake implements PokemonDao {
  public static final String INIT_DB_RECORDS_FAKE_KEY = "FakeDBRecordsKey";

  @Override
  public Map<String, String> queryPokemonPowers(List<String> ignore) {
    final var fakeDbRecords =
        AnyToAny.getMap(INIT_DB_RECORDS_FAKE_KEY, String.class, String.class, 3);
    // Simulating a Pokémon from HTTP response existing in DB through concatenation
    fakeDbRecords.putAll(AnyToAny.getMap(HTTP_RESPONSE_EXISTING_POKEMON_FAKE_KEY));
    return fakeDbRecords;
  }

  @Override
  public Map<String, String> queryAllPokemonPowers() {
    // Concat init state with the updates via `EntityAccessorFake` to simulate entries in the DB
    return Stream.concat(
            AnyToAny.<String, String>getMap(INIT_DB_RECORDS_FAKE_KEY).entrySet().stream(),
            MultiAnyToAny.<String, String, String>getCache().values().stream()
                .map(table -> Map.entry(table.get("name"), table.get("power"))))
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }
}

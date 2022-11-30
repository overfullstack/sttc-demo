package ga.overfullstack.pokemon.now.fake;

import static ga.overfullstack.pokemon.now.fake.PokemonHttpFake.HTTP_RESPONSE_EXISTING_POKEMON_FAKE_KEY;
import static ga.overfullstack.pokemon.now.fake.config.BeanName.POKEMON_DAO_FAKE;
import static java.util.stream.Collectors.toMap;

import ga.overfullstack.loki.dud.AnyToAny;
import ga.overfullstack.loki.dud.MultiAnyToAny;
import ga.overfullstack.pokemon.now.PokemonDao;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kotlin.collections.MapsKt;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * This is an example for a module-specific Fake. This makes use of the Dud to generate Fake data.
 */
@Component(POKEMON_DAO_FAKE)
public class PokemonDaoFake implements PokemonDao {
  public static final String INIT_DB_RECORDS_FAKE_KEY = "FakeDBRecordsKey";

  @Override
  public Map<String, String> queryPokemonPowers(List<String> ignore) {
    final var fakeDbRecords =
        AnyToAny.getMap(INIT_DB_RECORDS_FAKE_KEY, String.class, String.class, 3);
    // Simulating "Pokémon from HTTP response exists in DB" by merging
    // HTTP_RESPONSE_EXISTING_POKEMON_FAKE_KEY into INIT_DB_RECORDS_FAKE_KEY in Dud
    fakeDbRecords.putAll(AnyToAny.getMap(HTTP_RESPONSE_EXISTING_POKEMON_FAKE_KEY));
    return fakeDbRecords;
  }

  @Override
  public Map<String, String> queryAllPokemonPowers() {
    // Concat init state with the updates via `EntityAccessorFake` to simulate entries in the DB
    return MapsKt.plus(
        AnyToAny.getMap(INIT_DB_RECORDS_FAKE_KEY), getDataInsertedViaEntityAccessor());
  }

  @NotNull
  public static Map<String, String> getDataInsertedViaEntityAccessor() {
    return MultiAnyToAny.<String, String, String>getCache().values().stream()
        .collect(toMap(table -> table.get("name"), table -> table.get("power")));
  }
}

package ga.overfullstack.pokemon.now.fake;

import static ga.overfullstack.pokemon.now.fake.PokemonHttpFake.HTTP_RESPONSE_PRE_EXISTING_POKEMON_FAKE_KEY;
import static ga.overfullstack.pokemon.now.fake.TestConstants.POKEMON_DAO_FAKE;
import static ga.overfullstack.pokemon.now.fake.TestConstants.POKEMON_FIELD_TYPE_INFO;
import static ga.overfullstack.pokemon.now.fake.TestConstants.tableToPokemonMap;

import ga.overfullstack.legacy.Entity;
import ga.overfullstack.loki.dud.Dud;
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
    final var fakeDbRecords = tableToPokemonMap(
        Dud.getOrGenerateTableIfAbsent(INIT_DB_RECORDS_FAKE_KEY, 3, POKEMON_FIELD_TYPE_INFO, null));
    // Simulating "Some Pokémon from HTTP response pre-exist in DB" by merging
    // HTTP_RESPONSE_EXISTING_POKEMON_FAKE_KEY into INIT_DB_RECORDS_FAKE_KEY in Dud
    fakeDbRecords.putAll(tableToPokemonMap(Dud.getOrGenerateTableIfAbsent(HTTP_RESPONSE_PRE_EXISTING_POKEMON_FAKE_KEY, null, null, null)));
    return fakeDbRecords;
  }

  @Override
  public Map<String, String> queryAllPokemonPowers() {
    // Concat init state with the updates via `EntityAccessorFake` to simulate mix of pre-existed and new Pokémon in the DB
    return MapsKt.plus(
        tableToPokemonMap(Dud.getOrGenerateTableIfAbsent(INIT_DB_RECORDS_FAKE_KEY, null, null, null)),
        getDataInsertedViaEntityAccessor());
  }

  @NotNull
  public static Map<String, String> getDataInsertedViaEntityAccessor() {
    return (Map<String, String>) Dud.getOrGenerateTableIfAbsent(null, null, null, null).rowMap().entrySet().stream()
        .filter(rowEntry -> rowEntry.getKey() instanceof Entity)
        .collect(Collectors.toMap(
            rowEntry -> POKEMON_FIELD_TYPE_INFO.get("name").cast(rowEntry.getValue().get("name")),
            rowEntry -> POKEMON_FIELD_TYPE_INFO.get("name").cast(rowEntry.getValue().get("power"))
        ));
  }

}

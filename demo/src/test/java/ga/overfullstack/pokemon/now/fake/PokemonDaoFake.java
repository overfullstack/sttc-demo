package ga.overfullstack.pokemon.now.fake;

import static ga.overfullstack.pokemon.now.fake.PokemonHttpFake.FAKE_RESPONSE_KEY;
import static ga.overfullstack.pokemon.now.fake.config.BeanName.POKEMON_DAO_FAKE;

import ga.overfullstack.loki.dud.AnyToAny;
import ga.overfullstack.loki.dud.MultiAnyToAny;
import ga.overfullstack.pokemon.now.PokemonDao;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/** This is an example for a module-specific Fake. */
@Component(POKEMON_DAO_FAKE)
public class PokemonDaoFake implements PokemonDao {
  public static final String FAKE_DB_RECORDS_KEY = "FakeDBRecordsKey";

  @Override
  public Map<String, String> queryPokemonPowers(List<String> ignore) {
    return queryPokemonPowers();
  }

  @NotNull
  private static Map<String, String> queryPokemonPowers() {
    return Stream.concat(
            AnyToAny.<String, String>getMap(FAKE_RESPONSE_KEY).entrySet().stream().skip(2),
            AnyToAny.getMap(FAKE_DB_RECORDS_KEY, String.class, String.class, 3).entrySet().stream())
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }

  @Override
  public Map<String, String> queryAllPokemonPowers() {
    return Stream.concat(
            queryPokemonPowers().entrySet().stream(),
            MultiAnyToAny.<String, String, String>getCache().values().stream()
                .map(table -> Map.entry(table.get("name"), table.get("power"))))
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }
}

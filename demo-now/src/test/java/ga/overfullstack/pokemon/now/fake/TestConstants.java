package ga.overfullstack.pokemon.now.fake;

import static java.util.stream.Collectors.toMap;

import com.google.common.collect.HashBasedTable;
import java.util.Map;

public final class TestConstants {
  // ! TODO 27/12/22 gopala.akshintala: Derive this directly from the POJO 
  public static final Map<Object, ? extends Class<?>> POKEMON_FIELD_TYPE_INFO = Map.of(
      "name", String.class, "power", String.class);

  private TestConstants() {}

  public static final String POKEMON_DAO_FAKE = "PokemonDaoFake";
  public static final String POKEMON_HTTP_FAKE = "PokemonHttpFake";

  public static <D, E, V> Map<String, String> tableToPokemonMap(HashBasedTable<D, E, V> table) {
    return (Map<String, String>) table.rowMap().values().stream()
        .collect(toMap(
            row -> POKEMON_FIELD_TYPE_INFO.get("name").cast(row.get("name")), 
            row -> POKEMON_FIELD_TYPE_INFO.get("power").cast(row.get("power"))));
  }
}

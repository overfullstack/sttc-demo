package ga.overfullstack.powermock;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class PokemonEntity implements Entity {

  public PokemonEntity() {
    // Dummy, Do Nothing
  }

  // Dummy, assume it's the DB
  private final Map<String, String> data = new HashMap<>();

  @Override
  public void put(String field, String value) {
    data.put(field, value);
  }

  @Override
  public String get(@NotNull String field) {
    return data.get(field);
  }
}

package ga.overfullstack.powermock;

import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class PokemonEntity implements Entity {

  public PokemonEntity() {
    // Dummy, Do Nothing
  }

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

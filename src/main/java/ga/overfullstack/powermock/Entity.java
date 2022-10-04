package ga.overfullstack.powermock;

import org.jetbrains.annotations.NotNull;

public interface Entity {
  void put(String field, String value);

  String get(@NotNull String field);
}

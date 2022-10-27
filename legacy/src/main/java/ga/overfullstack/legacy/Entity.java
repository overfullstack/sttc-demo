package ga.overfullstack.legacy;

import static ga.overfullstack.legacy.DBUtil.connect;
import static ga.overfullstack.legacy.DBUtil.insertAndGetId;
import static ga.overfullstack.legacy.DBUtil.queryFromPokemon;
import static ga.overfullstack.legacy.DBUtil.updatePokemon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.exposed.dao.id.EntityID;
import org.jetbrains.exposed.sql.Table;

public abstract class Entity {
  private final EntityID<Integer> id;

  public Entity() {
    connect();
    id = insertAndGetId();
  }

  public String get(@NotNull String field) {
    return queryFromPokemon(id, field);
  }

  public void put(@NotNull String field, String value) {
    updatePokemon(id, field, value);
  }

  public abstract Table getTable();
}

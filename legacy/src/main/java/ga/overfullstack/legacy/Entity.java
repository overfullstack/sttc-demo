package ga.overfullstack.legacy;

import static ga.overfullstack.legacy.DBUtil.createAndGetId;
import static ga.overfullstack.legacy.DBUtil.queryFromPokemon;
import static ga.overfullstack.legacy.DBUtil.updatePokemon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.exposed.dao.id.EntityID;

/** Dummy legacy class representing link to a DB entity Hard wired to Powers table */
public class Entity {
  private final EntityID<Integer> id;

  public Entity() {
    id = createAndGetId();
  }

  public String get(@NotNull String field) {
    return queryFromPokemon(id, field);
  }

  public void put(@NotNull String field, String value) {
    updatePokemon(id, field, value);
  }
}

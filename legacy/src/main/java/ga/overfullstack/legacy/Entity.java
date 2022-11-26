package ga.overfullstack.legacy;

import static ga.overfullstack.legacy.DBUtil.createAndGetId;
import static ga.overfullstack.legacy.DBUtil.queryFromPokemon;
import static ga.overfullstack.legacy.DBUtil.updatePokemon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.exposed.dao.id.EntityID;

/** Dummy legacy class representing a link to a DB row in Powers table */
public class Entity {
  private final EntityID<Integer> id;

  public Entity() {
    id = createAndGetId();
  }

  public String get(@NotNull String field) {
    return queryFromPokemon(id, field);
  }

  /**
   * `put` directly updates in the DB. May not be a popular pattern, and we generally use a `save()`
   * or `commit()` call to update all fields at once. But this is implemented just to demonstrate
   * `put` as a legacy call.
   *
   * @param field
   * @param value
   */
  public void put(@NotNull String field, String value) {
    updatePokemon(id, field, value);
  }
}

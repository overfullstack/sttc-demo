package ga.overfullstack.powermock.after;

import ga.overfullstack.powermock.Entity;
import ga.overfullstack.powermock.EntityLoader;
import ga.overfullstack.powermock.Pokemon;
import ga.overfullstack.powermock.PokemonEntity;

class BeanToEntity {
  private final EntityAccessor entityAccessor;

  public BeanToEntity(EntityAccessor entityAccessor) {
    this.entityAccessor = entityAccessor;
  }

  PokemonEntity toEntity(Pokemon pokemon) {
    final var pokemonEntity = entityAccessor.loadNew(PokemonEntity.class);
    entityAccessor.put(pokemonEntity, "name", pokemon.getName());
    entityAccessor.put(pokemonEntity, "power", pokemon.getPower());
    return pokemonEntity;
  }
}

interface EntityAccessor {
  default <T extends Entity> T loadNew(Class<T> type) {
    return EntityLoader.loadNew(type);
  }

  default void put(Entity entity, String field, String value) {
    entity.put(field, value);
  }

  default String get(Entity entity, String field) {
    return entity.get(field);
  }
}

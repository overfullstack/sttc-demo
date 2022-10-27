package ga.overfullstack.powermock.after;

import ga.overfullstack.powermock.Pokemon;
import ga.overfullstack.powermock.PowerEntity;

class BeanToEntity {
  private final EntityAccessor entityAccessor;

  public BeanToEntity(EntityAccessor entityAccessor) {
    this.entityAccessor = entityAccessor;
  }

  PowerEntity toEntity(Pokemon pokemon) {
    final var pokemonEntity = entityAccessor.loadNew(PowerEntity.class);
    entityAccessor.put(pokemonEntity, "name", pokemon.getName());
    entityAccessor.put(pokemonEntity, "power", pokemon.getPower());
    return pokemonEntity;
  }

  public static void main(String[] args) {
    final var entityAccessor = new EntityAccessor() {};
    final var beanToEntity = new BeanToEntity(entityAccessor);
    final var pokemonBean = new Pokemon("pikachu", "static");
    final var pokemonEntity = beanToEntity.toEntity(pokemonBean);
    System.out.println(pokemonEntity);
  }
}

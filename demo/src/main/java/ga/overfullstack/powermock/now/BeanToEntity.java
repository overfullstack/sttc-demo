package ga.overfullstack.powermock.now;

import ga.overfullstack.legacy.Entity;
import ga.overfullstack.powermock.Pokemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BeanToEntity {
  private final EntityAccessor entityAccessor;
  private final Logger logger;

  public BeanToEntity(EntityAccessor entityAccessor, Logger logger) {
    this.entityAccessor = entityAccessor;
    this.logger = logger;
  }

  Entity toEntity(Pokemon pokemon) {
    final var pokemonEntity = entityAccessor.loadNew(Entity.class);
    entityAccessor.put(pokemonEntity, "name", pokemon.getName());
    entityAccessor.put(pokemonEntity, "power", pokemon.getPower());
    return pokemonEntity;
  }

  public static void main(String[] args) {
    final var beanToEntity = new BeanToEntity(new EntityAccessor() {}, LoggerFactory.getLogger(BeanToEntity.class));
    beanToEntity.play();
  }

  private void play() {
    final var pokemonBean = new Pokemon("pikachu", "static");
    final var pokemonEntity = toEntity(pokemonBean);
    logger.info("Pokemon Name: {}", pokemonEntity.get("name"));
    logger.info("Pokemon Power: {}", pokemonEntity.get("power"));
  }
}

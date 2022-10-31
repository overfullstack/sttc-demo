package ga.overfullstack.powermock.before;

import ga.overfullstack.legacy.Entity;
import ga.overfullstack.legacy.EntityLoader;
import ga.overfullstack.powermock.Pokemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BeanToEntity {
  private static final Logger LOGGER = LoggerFactory.getLogger(BeanToEntity.class);

  static Entity toEntity(Pokemon pokemon) {
    final var pokemonEntity = EntityLoader.loadNew(Entity.class);
    pokemonEntity.put("name", pokemon.getName());
    pokemonEntity.put("power", pokemon.getPower());
    return pokemonEntity;
  }

  public static void main(String[] args) {
    play();
  }

  private static void play() {
    final var pokemonBean = new Pokemon("pikachu", "static");
    final var pokemonEntity = BeanToEntity.toEntity(pokemonBean);
    LOGGER.info("Pokemon Name: {}", pokemonEntity.get("name"));
    LOGGER.info("Pokemon Power: {}", pokemonEntity.get("value"));
  }
}

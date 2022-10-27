package ga.overfullstack.powermock.before;

import ga.overfullstack.legacy.EntityLoader;
import ga.overfullstack.powermock.Pokemon;
import ga.overfullstack.powermock.PowerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BeanToEntity {
  private static final Logger logger = LoggerFactory.getLogger(BeanToEntity.class);

  static PowerEntity toEntity(Pokemon pokemon) {
    final var pokemonEntity = EntityLoader.loadNew(PowerEntity.class);
    pokemonEntity.put("name", pokemon.getName());
    pokemonEntity.put("power", pokemon.getPower());
    return pokemonEntity;
  }

  public static void main(String[] args) {
    final var pokemonBean = new Pokemon("pikachu", "static");
    final var pokemonEntity = BeanToEntity.toEntity(pokemonBean);
    logger.info(pokemonEntity.get("name"));
    logger.info(pokemonEntity.get("power"));
  }
}

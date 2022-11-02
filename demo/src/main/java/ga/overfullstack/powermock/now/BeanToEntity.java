package ga.overfullstack.powermock.now;

import ga.overfullstack.legacy.Entity;
import ga.overfullstack.legacy.LoadFromDBException;
import ga.overfullstack.loki.EntityAccessor;
import ga.overfullstack.powermock.Pokemon;
import java.util.Map;
import java.util.function.Function;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BeanToEntity {
  private final EntityAccessor entityAccessor;
  private final Logger logger;

  public BeanToEntity(EntityAccessor entityAccessor, Logger logger) {
    this.entityAccessor = entityAccessor;
    this.logger = logger;
  }

  private static final Map<Function<Pokemon, String>, Pair<String, Boolean>> FIELD_MAPPINGS =
      Map.of(
          Pokemon::name, new Pair<>("name", true),
          Pokemon::power, new Pair<>("power", false));

  Entity toEntity(@NotNull Pokemon pokemon) throws LoadFromDBException {
    final var pokemonEntity = entityAccessor.loadNew(Entity.class);
    FIELD_MAPPINGS.forEach(
        (sourceFn, destPair) -> {
          final var sourceValue = sourceFn.apply(pokemon);
          final var fieldName = destPair.getFirst();
          final var isFieldRequired = destPair.getSecond();
          if (isFieldRequired && sourceValue == null) {
            entityAccessor.put(pokemonEntity, fieldName, "");
          } else {
            entityAccessor.put(pokemonEntity, fieldName, sourceValue);
          }
        });
    return pokemonEntity;
  }

  public static void main(String[] args) throws LoadFromDBException {
    final var beanToEntity =
        new BeanToEntity(new EntityAccessor() {}, LoggerFactory.getLogger(BeanToEntity.class));
    beanToEntity.play();
  }

  private void play() throws LoadFromDBException {
    final var pokemonBean = new Pokemon("pikachu", "static");
    final var pokemonEntity = toEntity(pokemonBean);
    logger.info("Pokemon Name: {}", pokemonEntity.get("name"));
    logger.info("Pokemon Power: {}", pokemonEntity.get("power"));
  }
}

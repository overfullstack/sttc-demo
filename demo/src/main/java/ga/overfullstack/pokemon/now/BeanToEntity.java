package ga.overfullstack.pokemon.now;

import ga.overfullstack.legacy.Entity;
import ga.overfullstack.legacy.LoadFromDBException;
import ga.overfullstack.loki.EntityAccessor;
import ga.overfullstack.loki.LoggerSupplier;
import ga.overfullstack.pokemon.Pokemon;
import java.util.Map;
import java.util.function.Function;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanToEntity {
  private final EntityAccessor entityAccessor;
  private final Logger logger;

  @Autowired
  public BeanToEntity(EntityAccessor entityAccessor, LoggerSupplier loggerSupplier) {
    this.entityAccessor = entityAccessor;
    this.logger = loggerSupplier.supply(this.getClass());
  }

  private static final Map<Function<Pokemon, String>, Pair<String, Boolean>> FIELD_MAPPINGS =
      Map.of(
          Pokemon::name, new Pair<>("name", true),
          Pokemon::power, new Pair<>("power", false));

  public Entity updateInDB(@NotNull Pokemon pokemon) throws LoadFromDBException {
    final var pokemonEntity = entityAccessor.loadNew(Entity.class);
    FIELD_MAPPINGS.forEach(
        (sourceFn, destPair) -> {
          final var sourceValue = sourceFn.apply(pokemon);
          final var fieldName = destPair.getFirst();
          final boolean isFieldRequired = destPair.getSecond();
          if (isFieldRequired && sourceValue == null) {
            entityAccessor.put(pokemonEntity, fieldName, "");
          } else {
            entityAccessor.put(pokemonEntity, fieldName, sourceValue);
          }
        });
    return pokemonEntity;
  }

  public static void main(String[] args) throws LoadFromDBException {
    final var ctx = new AnnotationConfigApplicationContext(BeanToEntity.class);
    final var beanToEntity = ctx.getBean(BeanToEntity.class);
    beanToEntity.play();
  }

  private void play() throws LoadFromDBException {
    final var pokemonBean = new Pokemon("pikachu", "static");
    final var pokemonEntity = updateInDB(pokemonBean);
    logger.info("Pokemon Name: {}", pokemonEntity.get("name"));
    logger.info("Pokemon Power: {}", pokemonEntity.get("power"));
  }
}

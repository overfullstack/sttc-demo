package ga.overfullstack.pokemon.now;

import static ga.overfullstack.pokemon.now.Config.ENTITY_ACCESSOR_EXTENDED;

import ga.overfullstack.legacy.Entity;
import ga.overfullstack.legacy.LoadFromDBException;
import ga.overfullstack.loki.adapter.EntityAccessor;
import ga.overfullstack.loki.adapter.LoggerSupplier;
import ga.overfullstack.pokemon.now.PokemonCollector.Pokemon;
import java.util.Map;
import java.util.function.Function;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
class BeanToEntityMapper {
  private final EntityAccessor entityAccessor;
  private final Logger logger;

  @Autowired
  BeanToEntityMapper(
      @Qualifier(ENTITY_ACCESSOR_EXTENDED) EntityAccessor entityAccessor,
      LoggerSupplier loggerSupplier) {
    this.entityAccessor = entityAccessor;
    this.logger = loggerSupplier.supply();
  }

  private static final Map<Function<Pokemon, String>, Pair<String, Boolean>> FIELD_MAPPINGS =
      Map.of(
          Pokemon::name, new Pair<>("name", true),
          Pokemon::power, new Pair<>("power", false));

  Entity insertInDB(@NotNull Pokemon pokemon) throws LoadFromDBException {
    final var pokemonEntity = entityAccessor.loadNew(Entity.class);
    FIELD_MAPPINGS.forEach(
        (sourceFn, destPair) -> {
          final var sourceValue = sourceFn.apply(pokemon);
          final var fieldName = destPair.getFirst();
          final boolean isFieldRequired = destPair.getSecond();
          if (isFieldRequired && sourceValue == null) {
            logger.info("Required field {} is null, replacing with empty string", fieldName);
            entityAccessor.put(pokemonEntity, fieldName, "");
          } else {
            entityAccessor.put(pokemonEntity, fieldName, sourceValue);
          }
        });
    return pokemonEntity;
  }

  // --- Quick play ---
  public static void main(String[] args) throws LoadFromDBException {
    final var ctx = new AnnotationConfigApplicationContext(Config.class);
    final var beanToEntity = ctx.getBean(BeanToEntityMapper.class);
    beanToEntity.play();
  }

  private void play() throws LoadFromDBException {
    final var pokemonBean = new Pokemon("pikachu", "static");
    final var pokemonEntity = insertInDB(pokemonBean);
    logger.info("Pokemon Name: {}", pokemonEntity.get("name"));
    logger.info("Pokemon Power: {}", pokemonEntity.get("power"));
  }
}

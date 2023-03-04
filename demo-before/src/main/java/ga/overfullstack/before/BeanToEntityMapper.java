package ga.overfullstack.before;

import ga.overfullstack.before.PokemonCollector.Pokemon;
import ga.overfullstack.legacy.Entity;
import ga.overfullstack.legacy.EntityLoader;
import ga.overfullstack.legacy.LoadFromDBException;
import java.util.Map;
import java.util.function.Function;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Component directly interacts with Legacy Components (Emulated here with classes from {@link
 * ga.overfullstack.legacy} package from legacy module)
 */
class BeanToEntityMapper {
  private static final Logger LOGGER = LoggerFactory.getLogger(BeanToEntityMapper.class);
  static final String UNKNOWN = "unknown";
  private static final Map<Function<Pokemon, String>, Pair<String, Boolean>> FIELD_MAPPINGS =
      Map.of( // FieldMapper, Pair(FieldName, isRequiredField)
          Pokemon::name, new Pair<>("name", true),
          Pokemon::power, new Pair<>("power", false));

  static Entity insertInDB(@NotNull Pokemon pokemon) throws LoadFromDBException {
    // Entity acts like a reference to a Row in a table
    final var pokemonEntity = EntityLoader.loadNew(Entity.class);
    FIELD_MAPPINGS.forEach(
        (sourceFn, destPair) -> {
          final var sourceValue = sourceFn.apply(pokemon);
          final var fieldName = destPair.getFirst();
          final boolean isFieldRequired = destPair.getSecond();
          if (isFieldRequired && sourceValue == null) {
            LOGGER.info("Required field {} is null, replacing with {}", fieldName, UNKNOWN);
            pokemonEntity.put(fieldName, UNKNOWN);
          }
          pokemonEntity.put(fieldName, sourceValue); // 🐞 should be in else block
        });
    return pokemonEntity;
  }

  // --- Quick play ---
  public static void main(String[] args) throws LoadFromDBException {
    play();
  }

  private static void play() throws LoadFromDBException {
    final var pokemonBean = new Pokemon("pikachu", "static");
    final var pokemonEntity = BeanToEntityMapper.insertInDB(pokemonBean);
    LOGGER.info("Pokemon Name: {}", pokemonEntity.get("name"));
    LOGGER.info("Pokemon Power: {}", pokemonEntity.get("value"));
  }
}

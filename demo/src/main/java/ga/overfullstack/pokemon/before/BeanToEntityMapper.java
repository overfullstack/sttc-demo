package ga.overfullstack.pokemon.before;

import ga.overfullstack.legacy.Entity;
import ga.overfullstack.legacy.EntityLoader;
import ga.overfullstack.legacy.LoadFromDBException;
import ga.overfullstack.pokemon.Pokemon;
import java.util.Map;
import java.util.function.Function;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This Component directly interacts with Legacy Components
 * (Emulated here with classes from {@link ga.overfullstack.legacy} package from legacy module)
 */
@Component
public class BeanToEntityMapper {
  private static final Logger LOGGER = LoggerFactory.getLogger(BeanToEntityMapper.class);
  private static final Map<Function<Pokemon, String>, Pair<String, Boolean>> FIELD_MAPPINGS =
      Map.of( // FieldMapper, Pair(FieldName, isRequiredField)
          Pokemon::name, new Pair<>("name", true),
          Pokemon::power, new Pair<>("power", false));

  public static Entity insertInDB(@NotNull Pokemon pokemon) throws LoadFromDBException {
    // Entity acts like a reference to a Row in a table
    final var pokemonEntity = EntityLoader.loadNew(Entity.class);
    FIELD_MAPPINGS.forEach(
        (sourceFn, destPair) -> {
          final var sourceValue = sourceFn.apply(pokemon);
          final var fieldName = destPair.getFirst();
          final boolean isFieldRequired = destPair.getSecond();
          if (isFieldRequired && sourceValue == null) {
            pokemonEntity.put(fieldName, "");
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

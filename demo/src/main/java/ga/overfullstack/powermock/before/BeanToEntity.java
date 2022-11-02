package ga.overfullstack.powermock.before;

import ga.overfullstack.legacy.Entity;
import ga.overfullstack.legacy.EntityLoader;
import ga.overfullstack.legacy.LoadFromDBException;
import ga.overfullstack.powermock.Pokemon;
import java.util.Map;
import java.util.function.Function;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BeanToEntity {
  private static final Logger LOGGER = LoggerFactory.getLogger(BeanToEntity.class);
  private static final Map<Function<Pokemon, String>, Pair<String, Boolean>> FIELD_MAPPINGS =
      Map.of(
          Pokemon::name, new Pair<>("name", true),
          Pokemon::power, new Pair<>("power", false));

  static Entity toEntity(@NotNull Pokemon pokemon) throws LoadFromDBException {
    final var pokemonEntity = EntityLoader.loadNew(Entity.class);
    FIELD_MAPPINGS.forEach(
        (sourceFn, destPair) -> {
          final var sourceValue = sourceFn.apply(pokemon);
          final var fieldName = destPair.getFirst();
          final var isFieldRequired = destPair.getSecond();
          if (isFieldRequired && sourceValue == null) {
            pokemonEntity.put(fieldName, "");
          }
          pokemonEntity.put(fieldName, sourceValue); // 🐞
        });
    return pokemonEntity;
  }

  public static void main(String[] args) throws LoadFromDBException {
    play();
  }

  private static void play() throws LoadFromDBException {
    final var pokemonBean = new Pokemon("pikachu", "static");
    final var pokemonEntity = BeanToEntity.toEntity(pokemonBean);
    LOGGER.info("Pokemon Name: {}", pokemonEntity.get("name"));
    LOGGER.info("Pokemon Power: {}", pokemonEntity.get("value"));
  }
}

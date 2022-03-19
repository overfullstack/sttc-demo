package ga.overfullstack.pokemon.before;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

public class PokemonDemo {
  private static final Logger logger = LoggerFactory.getLogger(PokemonDemo.class);

  public static void main(String[] args) {
    final var pokemon = HttpUtil.fetchPokemon(5);
    logger.info("Pokemon fetched: {}", pokemon.stream().map(Pokemon::getName).collect(Collectors.joining(", ")));
    final var powers = DBUtil.queryPowers(pokemon);
    logger.info("Powers: {}", String.join(", ", powers));
  }

}

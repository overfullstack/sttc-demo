package ga.overfullstack.pokemon.before;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import com.squareup.moshi.Moshi;
import ga.overfullstack.pokemon.Pokemon;
import ga.overfullstack.pokemon.Results;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ImplementationVisibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PokemonDemo {
  private static final String POKE_URI = "https://pokeapi.co/api/v2/pokemon?limit=%d";
  private static final Logger logger = LoggerFactory.getLogger(PokemonDemo.class);

  public static void main(String[] args) throws IOException, InterruptedException {
    final var pokemon = fetchPokemonNames(5);
    logger.info(String.join(", ", DBUtil.queryPowers(pokemon)));
  }

  static List<Pokemon> fetchPokemonNames(int noOfPokemon) throws IOException, InterruptedException {
    final var uri = String.format(POKE_URI, noOfPokemon);
    final var response = HttpUtil.getStringHttpResponse(uri);
    return parseResponse(response);
  }

  private static List<Pokemon> parseResponse(HttpResponse<String> response) throws IOException {
    final var moshi = new Moshi.Builder().build();
    final var jsonAdapter = moshi.adapter(Results.class);
    return Objects.requireNonNull(jsonAdapter.fromJson(response.body())).results();
  }

  @Value.Style(
      visibility = ImplementationVisibility.PUBLIC,
      allParameters = true,
      typeImmutable = "*",
      typeAbstract = "*Def",
      defaults = @Value.Immutable(builder = false, copy = false)
  )
  public @interface Data {}

  @Data
  @Value.Immutable
  interface ResultsDef {
    List<Pokemon> results();
  }

  @Data
  @Value.Immutable
  interface PokemonDef {
    String name();
  }

}

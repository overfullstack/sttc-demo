package ga.overfullstack.pokemon.now.fake;

import static ga.overfullstack.pokemon.now.fake.config.BeanName.POKEMON_DAO_FAKE;

import ga.overfullstack.pokemon.now.PokemonDao;
import java.util.List;
import java.util.Map;
import kotlin.Pair;
import org.springframework.stereotype.Component;

/** This is an example for a module-specific Fake. */
@Component(POKEMON_DAO_FAKE)
public class PokemonDAOFake implements PokemonDao {
  @Override
  public Map<String, String> queryPokemonPowers(List<String> pokemonNames) {
    return Map.of(
        "pokemon1", "power1",
        "pokemon3", "power3");
  }

  @Override
  public void batchInsertPokemonPowers(List<Pair<String, String>> pokemonToPower) {
    // Do Nothing
  }

  @Override
  public Map<String, String> queryAllPokemonPowers() {
    return Map.of(
        "pokemon1", "power1",
        "pokemon2", "power2",
        "pokemon3", "power3");
  }
}

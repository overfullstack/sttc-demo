package ga.overfullstack.pokemon.before;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Map;
class PokemonDemoTest {
    @Test
    void fetchPokemonPower() {
      try (final var httpUtil = Mockito.mockStatic(HttpUtil.class)) {
        httpUtil.when(() -> HttpUtil.fetchPokemonPower(anyString())).thenReturn("power");
        assertEquals("power", HttpUtil.fetchPokemonPower("name"));
      }
    }

  @Test
  void queryAllPokemonPowers() {
    try (final var dbUtil = Mockito.mockStatic(DBUtil.class)) {
      final var dummy = Map.of("name", "power");
      dbUtil.when(DBUtil::queryAllPokemonPowers).thenReturn(dummy);
      assertEquals(dummy, DBUtil.queryAllPokemonPowers());
    }
  }
}

package ga.overfullstack.powermock.before;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;

import ga.overfullstack.powermock.EntityLoader;
import ga.overfullstack.powermock.Pokemon;
import ga.overfullstack.powermock.PokemonEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EntityLoader.class})
public class BeanToEntityTest {

  @Test
  public void toEntity() throws Exception {
    // 👹 Return a Mock entity
    final var mockPokemonEntity = PowerMockito.mock(PokemonEntity.class);
    PowerMockito.when(EntityLoader.loadNew(PokemonEntity.class)).thenReturn(mockPokemonEntity);
    PowerMockito.doNothing().when(mockPokemonEntity).put(anyString(), anyString());

    final var pokemonBean = new Pokemon("mockPokemon", "mockPower");
    PowerMockito.when(mockPokemonEntity.get("name")).thenReturn(pokemonBean.getName());
    PowerMockito.when(mockPokemonEntity.get("power")).thenReturn(pokemonBean.getPower());

    final var actualPokemonEntity = BeanToEntity.toEntity(pokemonBean);

    // 👹 Assert on the same Mock entity from above
    assertEquals("mockPokemon", actualPokemonEntity.get("name"));
    assertEquals("mockPower", actualPokemonEntity.get("power"));
  }
}

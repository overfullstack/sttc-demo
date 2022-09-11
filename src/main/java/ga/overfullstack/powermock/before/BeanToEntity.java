package ga.overfullstack.powermock.before;

import ga.overfullstack.powermock.EntityLoader;
import ga.overfullstack.powermock.Pokemon;
import ga.overfullstack.powermock.PokemonEntity;

class BeanToEntity {
  static PokemonEntity toEntity(Pokemon pokemon) {
    final var pokemonEntity = EntityLoader.loadNew(PokemonEntity.class);
    pokemonEntity.put("name", pokemon.getName());
    pokemonEntity.put("power", pokemon.getPower());
    return pokemonEntity;
  }
}

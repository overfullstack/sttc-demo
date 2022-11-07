package ga.overfullstack.pokemon.now.config;

import ga.overfullstack.pokemon.now.PokemonCollector;
import ga.overfullstack.pokemon.now.PokemonDao;
import ga.overfullstack.pokemon.now.PokemonHttp;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    PokemonCollector.class,
    PokemonHttp.class,
    PokemonDao.class
})
public class Config {
  
}

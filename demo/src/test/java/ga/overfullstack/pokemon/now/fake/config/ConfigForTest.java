package ga.overfullstack.pokemon.now.fake.config;

import ga.overfullstack.pokemon.now.fake.PokemonDAOFake;
import ga.overfullstack.pokemon.now.fake.PokemonHttpFake;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PokemonHttpFake.class, PokemonDAOFake.class})
public class ConfigForTest {}

package ga.overfullstack.pokemon.now.fake.config;

import ga.overfullstack.pokemon.now.fake.PokemonDaoFake;
import ga.overfullstack.pokemon.now.fake.PokemonHttpFake;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PokemonHttpFake.class, PokemonDaoFake.class})
public class ConfigForTest {}

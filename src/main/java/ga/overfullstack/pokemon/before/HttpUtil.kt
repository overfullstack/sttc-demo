package ga.overfullstack.pokemon.before

import org.http4k.client.JavaHttpClient
import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.format.Moshi.auto

private const val POKE_BASE_URI = "https://pokeapi.co/api/v2/"

class HttpUtil {
  companion object {
    @JvmStatic
    fun fetchAllPokemon(limit: Int): List<String> {
      val resultsLens = Body.auto<Results>().toLens()
      val pokemonApi = ClientFilters.SetBaseUriFrom(Uri.of(POKE_BASE_URI)).then(JavaHttpClient())
      val response: Response = pokemonApi(Request(Method.GET, "pokemon").query("limit", limit.toString()))
      return resultsLens(response).results.map { it.name }
    }
    
    @JvmStatic
    fun fetchPokemonPower(pokemonName: String): String {
      val abilitiesLens = Body.auto<Abilities>().toLens()
      val pokemonApi = ClientFilters.SetBaseUriFrom(Uri.of(POKE_BASE_URI)).then(JavaHttpClient())
      val response: Response = pokemonApi(Request(Method.GET, "pokemon/$pokemonName"))
      return abilitiesLens(response).abilities.first().ability.name
    }
  }
  private data class Pokemon(val name: String)

  private data class Results(val results: List<Pokemon>)

  private data class Ability(val name: String)

  private data class AbilityWrapper(val ability: Ability)
  private data class Abilities(val abilities: List<AbilityWrapper>)
}

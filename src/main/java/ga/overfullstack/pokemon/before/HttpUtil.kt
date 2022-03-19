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

class HttpUtil {
  companion object {
    private val body = Body.auto<Results>().toLens()

    @JvmStatic
    fun fetchPokemon(noOfPokemon: Int): List<Pokemon> {
      val pokemonApi = ClientFilters.SetBaseUriFrom(Uri.of("https://pokeapi.co")).then(JavaHttpClient())
      val response: Response = pokemonApi(Request(Method.GET, "/api/v2/pokemon").query("limit", noOfPokemon.toString()))
      return body(response).results
    }
  }
}

data class Pokemon(val name: String)

data class Results(val results: List<Pokemon>)

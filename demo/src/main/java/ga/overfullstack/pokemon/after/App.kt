@file:JvmName("App")

package ga.overfullstack.pokemon.after

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.Moshi.auto
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.slf4j.LoggerFactory
import java.util.Random

private val random = Random()

@JvmField
val POKEMON_OFFSET_TO_FETCH = random.ints(1, 100).findFirst().orElse(1)

@JvmField
val POKEMON_LIMIT_TO_FETCH = random.ints(1, 6).findFirst().orElse(1)

fun main() {
  val app: HttpHandler = { request ->
    val resultLens = Body.auto<Map<String, String>>().toLens()
    val pokemonCollector = PokemonCollector(
      object : PokemonDao {},
      object : PokemonHttp {},
      LoggerFactory.getLogger(PokemonCollector::class.java)
    )
    Response(Status.OK).with(
      resultLens of pokemonCollector.play(
        request.query("offset")?.toInt() ?: POKEMON_OFFSET_TO_FETCH,
        request.query("limit")?.toInt() ?: POKEMON_LIMIT_TO_FETCH
      )
    )
  }
  app.asServer(SunHttp(7000)).start()
}

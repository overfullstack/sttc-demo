@file:JvmName("App")

package ga.overfullstack.before

import ga.overfullstack.before.PokemonCollector.POKEMON_LIMIT_TO_FETCH
import ga.overfullstack.before.PokemonCollector.POKEMON_OFFSET_TO_FETCH
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.Moshi.auto
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
  val app: HttpHandler = { request ->
    val resultLens = Body.auto<Map<String, String>>().toLens()
    Response(Status.OK)
      .with(
        resultLens of
          PokemonCollector.play(
            request.query("offset")?.toInt() ?: POKEMON_OFFSET_TO_FETCH,
            request.query("limit")?.toInt() ?: POKEMON_LIMIT_TO_FETCH
          )
      )
  }
  app.asServer(SunHttp(7000)).start()
}

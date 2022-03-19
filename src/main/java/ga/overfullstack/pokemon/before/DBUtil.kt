@file:JvmName("DBUtil")

package ga.overfullstack.pokemon.before

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class DBUtil {

  private object Powers : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val power = varchar("power", 50)

    override val primaryKey = PrimaryKey(id, name = "PK_Powers_ID")
  }
  
  companion object {
    private val INIT_DATA = listOf(
      "pikachu" to "static",
      "bulbasaur" to "chlorophyll",
      "charmeleon" to "blaze",
      "squirtle" to "torrent",
      "eevee" to "adaptability",
    )
    init {
      Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
      transaction {
        SchemaUtils.create(Powers)
        Powers.batchInsert(INIT_DATA) { (name, power) ->
          this[Powers.name] = name
          this[Powers.power] = power
        }
      }
    }
    @JvmStatic
    fun queryPowers(pokemonList: List<Pokemon>): List<String> {
      return transaction {
        Powers.selectAll().andWhere { Powers.name inList pokemonList.map(Pokemon::name) }.map { it[Powers.power] }
      }
    }
  }
}

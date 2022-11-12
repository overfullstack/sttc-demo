@file:JvmName("DBUtil")

package ga.overfullstack.legacy

import ga.overfullstack.legacy.DBUtil.Powers.fieldStrToColumn
import ga.overfullstack.legacy.DBUtil.Powers.name
import ga.overfullstack.legacy.DBUtil.Powers.power
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class DBUtil {
  companion object {
    private val INIT_DATA = listOf(
      "pikachu" to "static",
      "bulbasaur" to "chlorophyll",
      "charmeleon" to "blaze",
      "squirtle" to "torrent",
      "eevee" to "adaptability"
    )

    init { // Singleton
      Database.connect(
        "jdbc:postgresql://localhost:5432/pokemon",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres"
      )
      transaction {
        SchemaUtils.create(Powers)
        Powers.batchInsert(INIT_DATA) { (name, power) ->
          this[Powers.name] = name
          this[Powers.power] = power
        }
      }
    }

    @JvmStatic
    fun queryFromPokemon(pokemonId: EntityID<Int>, fieldName: String): String? =
      fieldStrToColumn(fieldName)?.let { fieldCol ->
        transaction {
          Powers.select { Powers.id eq pokemonId }.single()[fieldCol as Column<String>]
        }
      }

    @JvmStatic
    fun queryPokemonPowers(pokemonNames: List<String>): Map<String?, String?> = if (pokemonNames.isNotEmpty()) {
      transaction {
        Powers.selectAll()
          .andWhere { name inList pokemonNames }
          .associate { it[name] to it[power] }
      }
    } else {
      emptyMap()
    }

    @JvmStatic
    fun queryAllPokemonWithPowers(): Map<String?, String?> = transaction {
      Powers.selectAll().associate { it[name] to it[power] }
    }

    @JvmStatic
    fun batchInsertPokemonPowers(pokemonToPower: List<Pair<String, String>>) = transaction {
      Powers.batchInsert(pokemonToPower) { (pokemonName, powerValue) ->
        this[name] = pokemonName
        this[power] = powerValue
      }
    }

    @JvmStatic
    fun updatePokemon(id: EntityID<Int>, field: String, value: String) =
      fieldStrToColumn(field)?.let { fieldCol ->
        transaction {
          Powers.update({ Powers.id eq id }) {
            it[fieldCol as Column<String>] = value
          }
        }
      }

    @JvmStatic
    fun updatePokemon(id: EntityID<Int>, fieldValue: Map<String, String>) =
      fieldValue.entries.forEach { (field, value) -> updatePokemon(id, field, value) }

    @JvmStatic
    fun createAndGetId() = transaction {
      Powers.insertAndGetId {
        it[name] = ""
      }
    }
  }

  object Powers : IntIdTable() {
    val name = varchar("name", 50)
    val power = varchar("power", 50).nullable()

    fun fieldStrToColumn(field: String) = columns.find { col -> col.name == field }
  }
}

package ai.rattlesnakes.server.models

import com.lucidchart.open.relate._
import com.lucidchart.open.relate.interp._
import play.api.Play.current
import play.api.db.DB
import scala.util.Random

case class Game(id: String)

object Game {

  private val random  = new Random()

  def init() = {
    DB.withConnection { implicit connection =>
      sql"""
        CREATE TABLE IF NOT EXISTS games (
          id int
        )
      """.execute()
    }
  }

  def createGame() = {
    val id = createId
    DB.withConnection { implicit connection =>
      sql"""
        INSERT INTO games VALUES ($id)
      """.execute()
    }
    Game(id)
  }

  def games = {
    DB.withConnection { implicit connection =>
      sql"""
        SELECT id FROM games
      """.asSeq(RowParser { row =>
        Game(row.string("id"))
      })
    }
  }

  private def createId = {
    val bytes = new Array[Byte](4)
    random.nextBytes(bytes)
    bytes.map(b => f"$b%02x").mkString
  }

}

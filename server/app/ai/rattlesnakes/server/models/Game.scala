package ai.rattlesnakes.server.models

import scala.collection.mutable
import scala.util.Random

case class Game(id: String)

object Game {

  val games = mutable.ListBuffer[Game]()
  private val random  = new Random()

  def createGame() = {
    val game = new Game(createId)
    games += game
    println(games)
    game
  }

  def clearGames() {
    games.clear()
  }

  private def createId = {
    val bytes = new Array[Byte](4)
    random.nextBytes(bytes)
    bytes.map(b => f"$b%02x").mkString
  }

}

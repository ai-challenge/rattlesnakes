package ai.rattlesnakes.server.net

import java.util.concurrent.Executors

import ai.rattlesnakes.server.models.Game
import java.net.ServerSocket
import java.io.PrintWriter
import play.api.Logger
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.control.NonFatal

class GameServer {

  private implicit val executionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(4))
  private var serverSocket = Option.empty[ServerSocket]

  def start() {
    Logger.info("Starting game server")
    serverSocket = Some(new ServerSocket(8020))
    Logger.debug("Started game server")
    Future {
      while (true) {
        val socket = serverSocket.get.accept()
        Future {
          val writer = new PrintWriter(socket.getOutputStream)
          val game = Game.createGame()
          writer.write(s"Welcome to game ${game.id}\n")
          writer.flush()
          socket.close()
        }
      }
    }
  }

  def stop() {
    try {
      Logger.info("Ending game server")
      serverSocket.foreach(_.close())
      Logger.debug("Ended game server")
    } catch {
      case NonFatal(e) =>
    }
    Game.clearGames()
  }

}

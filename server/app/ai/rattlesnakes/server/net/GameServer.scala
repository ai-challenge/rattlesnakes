package ai.rattlesnakes.server.net

import ai.rattlesnakes.server.Contexts
import java.net.ServerSocket
import java.net.Socket
import play.api.Logger
import play.api.Play.configuration
import play.api.Play.current
import scala.concurrent.Future
import scala.util.control.NonFatal

class GameServer {
  private var serverSocket = Option.empty[ServerSocket]

  private lazy val port = configuration.getInt("gameserver.port").get
  private lazy val timeoutMillis = configuration.getInt("gameserver.timeoutMillis").get

  private def handleConnection(socket: Socket) {
    socket.setSoTimeout(timeoutMillis)
    socket.setKeepAlive(true)

    Logger.info("New connection from " + socket.getInetAddress().toString() + ":" + socket.getPort())

    try {
      val gameClient = new GameClient(socket)
      gameClient.run()
    }
    catch {
      case e: Exception => {
        Logger.error("Game Client Error", e)
        throw e
      }
    }
    finally {
      Logger.info("Closing connection from " + socket.getInetAddress().toString() + ":" + socket.getPort())
      socket.close()
    }
  }

  private def listen() {
    while (serverSocket.isDefined) {
      val socket = serverSocket.get.accept()
      Future {
        handleConnection(socket)
      }(Contexts.gameClients)
    }
  }

  def start() {
    Logger.info("Starting game server")
    serverSocket = Some(new ServerSocket(port))
    Logger.debug("Started game server")

    Future {
      listen()
      Logger.info("Game server listening on port " + port)
    }(Contexts.gameServer)
  }

  def stop() {
    try {
      Logger.info("Ending game server")
      serverSocket.foreach(_.close())
      serverSocket = Option.empty
      Logger.debug("Ended game server")
    } catch {
      case NonFatal(e) =>
    }
  }
}

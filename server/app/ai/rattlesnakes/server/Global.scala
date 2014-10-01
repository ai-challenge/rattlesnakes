package ai.rattlesnakes.server

import ai.rattlesnakes.server.net.GameServer
import play.api.Application
import play.api.GlobalSettings

object Global extends GlobalSettings {

  val gameServer = new GameServer()

  override def onStart(app: Application) {
    gameServer.stop()
    gameServer.start()
  }

  override def onStop(app: Application) {
    gameServer.stop()
  }

}

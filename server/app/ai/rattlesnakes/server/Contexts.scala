package ai.rattlesnakes.server

import ai.rattlesnakes.server.concurrent.Executors

object Contexts {
  /**
   * Single thread for listening to the game server port
   */
  val gameServer = Executors.singleThreadExecutor("gameserver")

  /**
   * Thread pool for connected socket clients
   */
  val gameClients = Executors.cachedThreadPool("gameclient")
}

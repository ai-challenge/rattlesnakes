package ai.rattlesnakes.server.net

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class GameClient(socket: Socket) {
  private val socketOut = new BufferedWriter(new PrintWriter(socket.getOutputStream()))
  private val socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()))

  private def sendMessage(message: String) {
    socketOut.write(message)
    socketOut.newLine()
    socketOut.flush()
  }

  private def receiveMessage(): String = {
    socketIn.readLine()
  }

  def run() {
    sendMessage("Welcome to RattleSnakes™")

    for (i <- 0 until 10) {
      val message = receiveMessage()
      sendMessage("got '" + message + "'")
    }

    sendMessage("goodbye")
    socketIn.close()
    socketOut.close()
  }
}

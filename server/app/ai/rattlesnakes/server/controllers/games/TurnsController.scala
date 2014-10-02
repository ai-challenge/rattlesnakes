package ai.rattlesnakes.server.controllers.games

import play.api.mvc.{Action, Controller}

object TurnsController extends Controller {

  def get(gameId: Int, turn: Int) = Action {
    Ok
  }

  def put(gameId: Int, turn: Int, player: String) = Action {
    Ok
  }

}

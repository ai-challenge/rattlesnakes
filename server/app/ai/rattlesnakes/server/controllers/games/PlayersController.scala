package ai.rattlesnakes.server.controllers.games

import play.api.mvc.{Action, Controller}

object PlayersController extends Controller {

  def post(gameId: Int) = Action {
    Ok
  }

}


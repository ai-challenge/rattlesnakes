package ai.rattlesnakes.server.controllers.games

import ai.rattlesnakes.server.models.Game
import ai.rattlesnakes.server.views
import play.api.mvc.{Action, Controller}

object GamesController extends Controller {

  def get = Action { request =>
    Ok(views.html.game.list(Game.games))
  }

  def get(id: Int) = Action {
    Ok
  }

  def post = Action {
    Ok
  }

}

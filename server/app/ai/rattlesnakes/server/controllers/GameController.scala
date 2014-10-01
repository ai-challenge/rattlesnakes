package ai.rattlesnakes.server.controllers

import ai.rattlesnakes.server.models.Game
import ai.rattlesnakes.server.views
import play.api.mvc.Action
import play.api.mvc.Controller

object GameController extends Controller {

  def list() = Action { request =>
    Ok(views.html.game.list(Game.games))
  }

}

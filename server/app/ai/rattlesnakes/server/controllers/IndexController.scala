package ai.rattlesnakes.server.controllers

import play.api.mvc.{Action, Controller}

object IndexController extends Controller {

  def get = Action {
    Ok//Redirect(routes.games.GamesController.get())
  }

}

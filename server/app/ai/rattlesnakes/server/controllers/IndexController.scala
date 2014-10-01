package ai.rattlesnakes.server.controllers

import play.api.mvc.Controller

class IndexController extends Controller {

  def get() = Redirect(routes.GameController.list())

}

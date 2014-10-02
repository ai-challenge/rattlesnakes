package ai.rattlesnakes.server.controllers.games

import play.api.mvc.{Action, Controller}

object StatusController extends Controller {

   def get(gameId: Int) = Action {
     Ok
   }

 }


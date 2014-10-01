# RattleSnake™

Can you write an AI to get the highest score in this RattleSnake™ game?


Write a JSON HTTP REST API client to play this game

#Game Description

The game is played on **an _infinite_** rectangular grid, each player controls a snake which can move UP, DOWN, LEFT, or RIGHT. Moves are made simultaneously and if your snake runs into an obstacle it is terminated. The grid contains the other snakes, walls, apples, and mice.

The objective of the game is to earn the most points. Points are earned by eating apples and mice, and staying alive longer than other players.

Your snake dies when you run into an obstacle (wall or other snake).

Each turn your snake either goes straight or turns. When your snake moves, it leaves a trail behind resembling a moving snake. Your snake has a length and the tail will be a fixed number of units away from the head. When you eat apples or mice the length of your snake will increase.

The game play is simultaneous. If two players move to the same cell in their move or they both hit an obstacle they both die at the same time.

Turns are made every 500ms. If your AI does not issue a command your snake will go forward in its current direction.

When you request the state of the game you receive information about the  grid, the current turn, and when the next turn will take place.

You can queue up actions with the server by sending the turn index along with the action you would like your snake to take on that turn. You can also cancel pending actions.


##Scoring
You can earn points in the following ways:

* Eat an apple (10 pt)
* Eat a mouse (25 pts)
* Be alive when another snake dies (50 pts)


##Eating Apples and Mice
When your snake eats an apple or a mouse the length of the snake increases as follows:

* Eating an apple: Length is increased by 1
* Eating a mouse: Length is increased by 2

##Conditions for end of game

When one of these conditions happen the game is over.

* All snakes die
* The last remaining snake has the highest score
* The last remaining snake has had 500 turns alone
* No apples or mice have been eaten in 500 consecutive turns


#Protocol

This is the hardest part of creating the game server. Defining the protocol.

Functionality we need to provide:

* Creating a WebSocket
* Create and join games
* Auto join players
* Retrieve game state
    * current turn
    * positions of other snakes
        * Array of coordinates and how long they will grow to
    * positions of walls
    * position of mice (along with next 3 moves)
    * positions of apples
    * current scores
* Move snake, wait til next turn, return state.
* wait until start of turn X then return state.

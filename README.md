# RattleSnake™

Can you write an AI to get the highest score in this RattleSnake™ game?


Write an ai client to play this game via sockets

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

* Creating a WebSocket - Barlocker
* <del> Create and join games - Stringham </del>
* <del> Auto join players - Stringham </del>
* Retrieve game state - Paul
    * current turn
    * positions of other snakes
        * Array of coordinates and how long they will grow to
    * positions of walls
    * position of mice (along with next 3 moves)
    * positions of apples
    * current scores
* Move snake, wait til next turn, return state. - Barlocker



###Creating a game

Connect to the server on port ```8020``` and send the following command
```
new-game <num-players> [<public> <board-width> <board-height>]
```

####Required Parameter

```<num-players>```  is a required field that determines how many snakes will be on the board. Must be between 2 and 4.

####Optional Parameters

```<public>``` Either ```0``` or ``1``. ```0``` means that players must join this game by id, and ```1``` means this game can be auto-joined. Defaults to ```0```

```<board-width>``` and ```<board-height>``` a number specifying how many cells wide and high the board is, respectively. If ```<board-width>``` is specified ```<board-height``` is required. Must be a number between 20 and 100.

If successful, you will receive a response of

```
created <game-id>
```
where ```<game-id>``` is the id of the game that was created. You will automatically join that game and wait for it to begin.



###Joining a game

There are two ways to join a game, you can join by id, or be automatically placed in an available game. 

To join a specific game connect to the server on port ```8020``` and send the following command

```
join <game-id>
```

If the game-id is valid and there is room for another player you will receive the response

```
welcome to game <game-id> <num-current-players>/<total-players>
```

To be automatically placed in an available game you can optionally request the number of opponents. Connect and send:

```
autojoin [<num-players]
```

Where ```<num-players>``` is an optional parameter of the desired game size you want to participate.

You will receive the response

```
welcome to game <game-id> <num-current-players>/<total-players>
```
Auto joining a game will create a game if no games are currently waiting for new players.

Once you have succesfully joined a game, continue listening on the port. You will be notified when each player joins and when the game starts.


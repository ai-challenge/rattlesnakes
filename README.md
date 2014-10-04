# RattleSnake™

Can you write an AI to get the highest score in this RattleSnake™ game?

Write an AI client to play this game via sockets

#Game Description

The game is played on **an _infinite_** rectangular grid, each player controls a snake which can move UP, DOWN, LEFT, or RIGHT. Moves are made simultaneously and if your snake runs into an obstacle it is terminated. The grid contains the other snakes, walls, apples, and mice.

The objective of the game is to earn the most points. Points are earned by eating apples and mice, and staying alive longer than other players. In the case of a tie, the player that died last will win. If a tie still exists, the player that ate the most mice will win. If a tie still exists, the player that ate the most apples will win. If a tie still exists, rejoice in your shared victory.

Your snake dies when you run into an obstacle (wall or other snake). After your death, your snake will shrivel up from tail to head. In other words, the head will not move, but the tail will continue to move. After the tail meets the head, the snake will be removed from the game. If you disconnect, your snake will die.

Each turn your snake either goes straight or turns. When your snake moves, it leaves a trail behind resembling a moving snake. Your snake has a length and the tail will be a fixed number of units away from the head. When you eat apples or mice the length of your snake will increase.

The game play is simultaneous. If two players move to the same cell in their move or they both hit an obstacle they both die at the same time.

Turns are made every 500ms to begin with, and get faster as time goes on. If your AI does not issue a command your snake will go forward in its current direction.

When you request the state of the game you receive information about the grid, the current turn, and when the next turn will take place.

Walls and apples are immovable. Snakes and mice move freely. Your snake can detect the next few moves of all the mice on the grid. However, a mouse will change its plans if a snake gets in its way.

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

##Visualization-Tool

Connect to the server on port 80, and follow the UI.

#Protocol

The game is played by creating a socket, connecting to port 8020 on the server, and exchanging commands with the server. Every command (requests and responses) in the protocol is ended by a single newline character (\n).


##Creating a game

###Request

To create a game specifying all options, send this message to the server.

```
new-game <num-players> <board-width> <board-height> <percent-walls> <percent-apples> <percent-mice> <public> <your-name>
```

```<num-players>``` determines how many snakes will be on the board. The server will wait to start the game until this many players have connected. Must be between 2 and 4.

```<board-width>``` and ```<board-height>``` are numbers specifying how many cells wide and high the board is, respectively. If ```<board-width>``` is specified ```<board-height>``` is required. Must be a number between 20 and 100.

```<percent-walls>```, ```<percent-apples>```, and ```<percent-mice>``` are all floating point numbers, between 0.0 and 1.0, that will determine the percent of squares on the board that will contain those items. The sum of these numbers must be less than 1.0, and is recommended to be less than 0.5.

```<public>``` is ```1``` if others can autojoin and ```0``` if they must use the ```<game-id>``` to join.

```<your-name>``` is the name of your snake, must be unique per game, and will be reported in the [visualization tool](#Visualization-tool), [game status](#Game-Status), and joined/left resonses.

###Response

If successful, you will receive a response of
```
created <game-id>
```
where ```<game-id>``` is the id of the game that was created. You will automatically join that game and wait for it to begin.

If any parameter is invalid, you will receive a response of
```
error <error-message>
```
where ```<error-message>``` is a human readable string of what went wrong.


##Joining a specific game

There are two ways to join a game, you can join a specific game by id, or be automatically placed in an available game.


###Request

To join a specific game by id, send the following command

```
join <game-id> <your-name>
```

```<game-id>``` is given to you when you [created a game](#Creating-a-game).

```<your-name>``` is the name of your snake, must be unique per game, and will be reported in the [visualization tool](#Visualization-Tool), [game status](#Game-Status), and joined/left responses.

###Response

When joining a game, you may not be the last player to join. Because of this, multiple responses will be given. The first response indicates a successful connect, and is given immediately. Then, responses are given as players join or leave. The final response indicates the start of the game, and will only be given when enough players have connected.

On a successful join, the first response will be
```
waiting <game-id>
```

When a player joins (or when you join a game with existing players), you will receive
```
joined <player-name> <current-players>/<total-players>
```

And when a player disconnects, you will receive
```
left <player-name> <current-players>/<total-players>
```

The final response, after all players have joined, will be
```
starting <game-status>
```

```<game-status>``` is a JSON blob that represents the [game status](#Game-Status)


On a failure during the join command, only one response will be given.
```
error <error-message>
```
where ```<error-message>``` is a human readable string of what went wrong.


##Auto joining a game

###Request

To be automatically placed in an available game, send:

```
autojoin [<num-players>]
```

Where ```<num-players>``` is an optional parameter of the desired game size in which you want to participate.

###Response

The success and failure responses for automatically joining games are identical to [joining a specific game](#Joining-a-specific-game).


##Make Move

###Request

On every turn, your snake has 3 options. Turn left, turn right, or keep going straight (noop). If you don't send a move, your snake will perform a noop. You can send your move to the server with this command

```
move left
```
or
```
move right
```
or
```
move noop
```

###Response

Once your move has been sent, the server will wait until the end of the current turn to make a response. It is possible, then, to send your move command on the wrong turn. The faster your computations are, the more likely you are to make a move in time.

The response from your move will vary, because many things can happen. Here is a list of responses. The first response that matches is the one you will get.

When the game is over, either because you won, so many moves occurred, or whatever, the response will be

```
game-over
<end of game response>
```

The end of game response is found [here](#End-of-game)

When you die, and the game isn't over yet, the response will be

```
dead
state <game-status>
```

The default response, given after most turns, will be

```
state <game-status>
```


##End of game

###Request

After your snake has died, you have the option to wait for the results of the game. This is wise, because it is not always the last snake alive that wins. To wait for the end of the game, send

```
wait end-of-game
```

###Response

The socket will wait for the end of the game before sending any more information. You will only get the end of game message if you are waiting for it or if you just made a move, and the game is over. The message will look like this:

```
end-of-game <end-of-game-status>
```

where ```<end-of-game-status>``` is a minified JSON blob which is printed readably here:

```json
{
	"id": "<game-id>",
	"winners": ["<player-name>"],
	"turns": 172,
	"players": {
		"<player-name>": {
			"apples": 9,
			"mice": 2,
			"length": 17,
			"moves": 168,
			"died": false,
			"winner": true,
			"score": 190
		}
	}
}
```

##Game status

###Request

To get the current state of the game, send
```
state
```

###Response

The game state is given whenever requested or mentioned in the protocol. The message looks like this
```
state <game-status>
```

```<game-status>``` is a minified JSON blob which is printed readably here:

```json
{
	"id": "<game-id>",
	"turn": 3,
	"time": 500,
	"width": 100,
	"height": 50,
	"obstacles": [
		[0, 0],
		[0, 49],
		[99, 49],
		[99, 0]
	],
	"apples": [
		[1, 19],
		[71, 32]
	],
	"mice": [{
		"at": [44, 13],
		"next": [
			[44, 12],
			[45, 12],
			[46, 12]
		]
	}, {
		"at": [72, 44],
		"next": [
			[72, 43],
			[72, 42],
			[71, 42]
		]
	}],
	"players": {
		"<player-name>": {
			"growth": 2,
			"died": false,
			"vertices": [
				[72, 42],
				[61, 42],
				[61, 36]
			]
		}
	}
}
```

In the game status, the player's ```growth``` represents the number of turns the tail will remain in the same position while the snake is growing. The ```vertices``` of a player's snake begin with the snake's head and end with the tail. All coordinates are given as 2-element arrays with ```[x, y]``` format with ```[0,0]``` being the top left cell. Along the X-axis, values increase towards the right edge of the board. Along the Y-axis, values increase towards the bottom edge of the board. The ```time``` indicates how many milliseconds until the end of this turn.

# API

The rattlesnakes server has (more or less) a RESTful API.

## GET /games

Get a list of current games.

## POST /games

Create a new game. You will be given a game id.

## GET /games/:gameId

```JSON
{
	"id": "Foo bar",
	"obstacles": [
		{
			"coordinate": {
				"x": 5,
				"y": 5
			}
		}, {
			"coordinate": {
				"x": 5
				"y": 6
			}
		}
	],
	"snakes": [
		{
			"id": 0,
			"name": "Snake A"
		}, {
			"id": 1,
			"name": "Snake B"
		}, {
			"id": 2,
			"name": "Snake C"
		}

	]
}
```

## POST /games/:gameId/players

Join the specified game. The server will respond once enough players have joined.
You will be given a player key that you will use to make your moves.

## GET /games/:gameId/status

Get the turn number and time remaining for the current turn.

```JSON
{
	"turn": 10,
	"time": 1000
}
```

## GET /games/:gameId/turns/:turn

Get the state of the game at the specified turn.


```JSON
{
	"apple": [
		{
			"coordinate": {
				"x": 4,
				"y": 1
			}
		}
	],
	"mice": [
		{
			"coordinate": {
				"x": 3,
				"y": 2
			},
			"next_moves": [
				{
					"x": 2,
					"y": 2
				}
			]
		}
	],
	"snakes": [
		{
			"id": 0,
			"coordinates": [
				{
					"x": 0,
					"y": 0
				}, {
					"x": 0,
					"y": 1
				}
			],
			"extra_length": 0
		}, {
			"id": 1,
			"coordinates": [
				{
					"x": 3,
					"y": 3
				}, {
					"x": 3,
					"y": 4
				}
			],
			"extra_length": 1
		}
	]
}
```

## PUT /games/:gameId/turns/:number/:player

Make a move. If the turn number is invalid (i.e. is not the current turn), the server responds with an error.
`:player` is the player key you received upon joining the game.
The server will respond once turn is over, i.e. once all players have made a move or the time is up.

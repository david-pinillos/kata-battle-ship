# Battle ships game kata

This is a project to do a practice, to learn some features in Kotlin.

Main test class are in [test folder](src/test/java/com/kata/battleshipsgame/service/).

You have some tests that call main class [BattleShipsGame](src/main/java/com/kata/battleshipsgame/service/BattleShipsGame.kt) where a match is played.

This class receives:

 - two boards with the boats of each player
 - two list of plays (coordinates to fire to the opponent boats)
 - first player to play

It will simulate a match, and it will return the results of it (winner if the match ended, etc).

Before the match, it will validate all the imput parameters (boards) are correct.

Rules:

- Boats
  - at least 1 boat and max 5 boats in each board
  - boat size needs to be between 2 and 5 cells
- Boards
  - required: board rows number = board col number
  - boards rows and columns needs to be >= 3 and <=9
  - boat cells in the board need to be >20% and <80%

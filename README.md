# Roll

A 3D Game Created in Java. The aim of the game is to avoid falling from the platform and make it to the finish line.

## How to get It Running?
This project written in eclipse.
- Import the the "Game" folder as a Java project
- Add all the external Jars from the "JavaFX" Folder

## Controls
- A is to move left
- D is to move right

## Classes
- Main
  - Responsible for the initialzing things, such as properly setting up JavaFX as well as keyboard inputs.
  - Controls the scene in javaFX as well as add lighting
- Player
  - Responsible for the player object (the Sphere)
  - Controls the movement of the player as well as the death of the player.
- Tile
  - Initializes and sets up each block
- Map
  - Dynamically renders the blocks according to the maps text file given
- EntryProperties
  - A class to store the name and the score for the leaderboard
- Leaderboard
  - Class to store all the entries and display them for highest score to lowest

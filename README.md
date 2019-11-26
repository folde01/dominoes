# Dominoes

This is a text-based dominoes game written in Scala. It is a piece of coursework I completed for the Software Design and Programming Module of the MSc Computer Science program at Birkbeck. The code can be found [here](https://github.com/folde01/dominoes/dominoes).

## How to run it from the command line (tested on Ubuntu 16.04 with Scala installed):

git clone https://github.com/folde01/dominoes
cd dominoes
scala -cp . dominoes.DominoesApp6

## Project notes:

My assignment was to write the user interface in Scala. Java classes for game engine and an API for it were provided by the teacher (see dominoes.zip). 

Most of my code is object-oriented, but functional programming is used in a few places - see, for example, in dominoes/players. 

ScalaCheck was used for testing and Scaladoc for documentation.

It received a distinction mark, and while the user interface obviously needs improvement it plays in both two-player (human vs human), single-player (human vs computer) and zero-player mode (computer vs computer, for testing purposes). The computer side does no more than play a valid move.

## To recompile:

scalac -cp . dominoes/Logger.scala dominoes/ConsoleLogger.scala dominoes/DominoesPrintUtil6.scala dominoes/QuitException.scala dominoes/NewGameException.scala dominoes/players/DominoPlayer6.scala dominoes/DominoUI6.scala dominoes/DominoesApp6.scala


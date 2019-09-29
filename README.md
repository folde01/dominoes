# Dominoes

One of the bigger pieces of my MSc coursework was a text-based dominoes game written in Scala, the code for which can be found [here](https://github.com/folde01/dominoes). The teacher provided the game engine, with the teaching aim apparently being "development isn't always so greenfield, so here's some code and an API for it, and some requirements for an interface... go write one." 

Scala being both object-oriented and functional, you can find examples of each paradigm in the code, especially in the players sub-folder. It uses ScalaCheck for testing.

It received a distinction mark but I don't like how I organised the code - namely, six different 'versions' all dumped in the same folder. There should've at least been a folder for each version - or, even better, I should have just used git properly. That said, the it played smoothly in both single-player and two-player mode without any obvious issues. Single-player is pretty dumb, though: the computer side does no more than play a valid move.

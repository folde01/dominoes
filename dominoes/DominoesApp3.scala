package dominoes

import dominoes._
 
object DominoesApp3 extends App { 

  println("\nWelcome to Dominoes\n")

  print("Enter name of player 1: ")
  val name1 = scala.io.StdIn.readLine()
  print("Enter name of player 2: ")
  val name2 = scala.io.StdIn.readLine()

  val cubby = new CubbyHole

  val human = true
  val player1 = new players.DominoPlayer3(cubby, human)
  player1.setName(name1)

  val computer = false
  val player2 = new players.DominoPlayer3(cubby, computer)
  player2.setName(name2)

  val ui = new DominoUI3
  ui.setCubby(cubby)
  val goal = 100
  val pips = 6
  val game = new Dominoes(ui, player1, player2, goal, pips)
  val winner = game.play

  println("\n\n" + winner.getName + " WINS!")
}

package dominoes

import dominoes._

object DominoesApp2 extends App { 
  val p1 = new players.DominoPlayer1
  val p2 = new players.DominoPlayer1
  val ui = new DominoUI2
  val goal = 100
  val pips = 6
  val game = new Dominoes(ui, p1, p2, goal, pips)
  val winner = game.play
  println("winner: " + winner.getName)
}

package dominoes

import dominoes._

object DominoesApp extends App { 
  val ui = new DominoUI1
  val p1 = new players.DominoPlayer1
  val p2 = new players.DominoPlayer1
  val goal = 100
  val pips = 6
  val game = new Dominoes(ui, p1, p2, goal, pips)
  val winner = game.play
  println("winner: " + winner.getName)
}

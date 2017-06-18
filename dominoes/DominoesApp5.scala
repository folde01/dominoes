package dominoes

import dominoes._
 
object DominoesApp5 extends App { 

  println("\nWelcome to Dominoes\n")

  print("Enter name of Player 1: ")
  val name1 = scala.io.StdIn.readLine()
  val is1Human = askIfPlayerHuman(1)

  print("Enter name of Player 2: ")
  val name2 = scala.io.StdIn.readLine()
  val is2Human = askIfPlayerHuman(2)

  val cubby = new CubbyHole

  val player1 = new players.DominoPlayer5(cubby, is1Human)
  player1.setName(name1)

  val player2 = new players.DominoPlayer5(cubby, is2Human)
  player2.setName(name2)

  val goal = askForGoal

  val ui = new DominoUI5
  ui.setCubby(cubby)
  val pips = 6
  val game = new Dominoes(ui, player1, player2, goal, pips)
  val winner = game.play

  val winString =
    s"\n\n\n\n-- GAME OVER -- ${winner.getName} WINS WITH ${winner.getPoints} POINTS! --\n\n\n\n"
  println(winString)

  def askForGoal: Int = { 
    print("How many points should it take to win? ")
    val entered = scala.io.StdIn.readLine().trim
    val regexIsInt = "^[0-9]+$"
    val n = 
      if (entered.matches(regexIsInt)) entered.toInt 
      else 0

    if (n > 0) n
    else { 
      println("Invalid goal. Try again.")
      askForGoal
    }
  }

  def askIfPlayerHuman(playerNum: Int): Boolean = { 
    print("Is Player " + playerNum + " human? (y/n) ")
    val entered = scala.io.StdIn.readLine().trim
    entered match { 
      case "y" => true 
      case "n" => false 
      case _ => askIfPlayerHuman(playerNum)
    }
  }
}

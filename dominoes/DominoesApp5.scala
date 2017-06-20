package dominoes

import dominoes._
import dominoes.DominoesUtil5._

import util.control.Breaks._

object DominoesApp5 extends App { 

  def askForGoal: Int = { 
    val entered = askUser("How many points should it take to win?")
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
    val entered = askUser("Is Player " + playerNum + " human? (y/n)")
    entered match { 
      case "y" => true 
      case "n" => false 
      case _ => askIfPlayerHuman(playerNum)
    }
  }

  def askForPlayerName(playerNum: Int): String = 
    askUser(s"Enter name of Player $playerNum:")

  def runGame { 

    println("\n\nWelcome to Dominoes")
    println("\nInstructions:")
    println("\nAt any prompt that follows, you can always type '..' to start a new game,")
    println("'qq' to quit, or 'h' for help remembering these instructions.")

    println("\nLet's get started!")

    val name1 = askForPlayerName(1)
    val name2 = askForPlayerName(2)
    val is1Human = askIfPlayerHuman(1)
    val is2Human = askIfPlayerHuman(2)
    val goal = askForGoal

    val cubby = new CubbyHole

    val player1 = new players.DominoPlayer5(cubby, is1Human)
    player1.setName(name1)

    val player2 = new players.DominoPlayer5(cubby, is2Human)
    player2.setName(name2)

    val ui = new DominoUI5
    ui.setCubby(cubby)
    val pips = 6
    val game = new Dominoes(ui, player1, player2, goal, pips)

    val winner = game.play

    val winString =
      s"\n\n-- GAME OVER -- ${winner.getName} WINS WITH ${winner.getPoints} POINTS! --\n\n"
    println(winString)

  }

  // main loop

  while (true) { 
    breakable { 
      try { 
        runGame
      } catch { 
        case _: NewGameException => { 
          println("\n\n. . . N E W   G A M E . . .")
          break  
        }
        case _: QuitException => { 
          println("\n\nGOODBYE!\n\n")
          System.exit(0) 
        }
        case _: Throwable => System.exit(0) 
      }
    }
  }
}

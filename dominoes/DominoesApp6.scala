package dominoes

import dominoes._
import dominoes.DominoesUtil6._
import util.control.Breaks._

object DominoesApp6 extends App { 

  private val debug = false

  private def askForGoal: Int = { 
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

  private def askIfPlayerHuman(playerNum: Int): Boolean = { 
    val entered = askUser("Is Player " + playerNum + " human? (y/n)")
    entered match { 
      case "y" => true 
      case "n" => false 
      case _ => askIfPlayerHuman(playerNum)
    }
  }

  private def askForPlayerName(playerNum: Int): String = 
    askUser(s"Enter name of Player $playerNum:")

  private def runGame { 

    println("\n\nWelcome to Dominoes")
    println("\nInstructions: At any prompt that follows, you can always type '..' to start") 
    println("a new game, 'qq' to quit, or 'h' for help remembering these instructions.")

    println("\nLet's get started!")

    val name1 = askForPlayerName(1)
    val name2 = askForPlayerName(2)
    val is1Human = askIfPlayerHuman(1)
    val is2Human = askIfPlayerHuman(2)
    val goal = askForGoal

    val cubby = new CubbyHole

    val player1 = new players.DominoPlayer6(cubby, is1Human, debug)
    player1.setName(name1)
    //player1.setLogger(new ConsoleLogger("___PLAYER_1"))

    val player2 = new players.DominoPlayer6(cubby, is2Human, debug)
    player2.setName(name2)
    //player2.setLogger(new ConsoleLogger("___PLAYER_2"))

    val ui = new DominoUI6
    //ui.setLogger(new ConsoleLogger("___UI"))
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

package dominoes

import dominoes._
import dominoes.DominoesUtil6._
import util.control.Breaks._

/** Object to run dominoes game. Sets up both players (with user input), the UI
  * and runs main game loop.
  */
object DominoesApp6 extends App { 


  /** This game logs debugging messages if debug is set to true 
    */
  private val debug = false


  /** Ask user for the number of points required to win.
    */
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


  /** Ask user if player being set up is human (as opposed to non-interactive). 
    * @param playerNum The player's number (1 or 2) 
    */ 
  private def askIfPlayerHuman(playerNum: Int): Boolean = { 
    val entered = askUser("Is Player " + playerNum + " human? (y/n)")
    entered match { 
      case "y" => true 
      case "n" => false 
      case _ => askIfPlayerHuman(playerNum)
    }
  }


  /** Ask user for name of player being set up. 
    * @param playerNum The player's number (1 or 2) 
    */ 
  private def askForPlayerName(playerNum: Int): String = 
    askUser(s"Enter name of Player $playerNum:")


  /** Run a single dominoes game
    */
  private def runSingleGame { 
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

    val player2 = new players.DominoPlayer6(cubby, is2Human, debug)
    player2.setName(name2)

    val ui = new DominoUI6
    ui.setCubby(cubby)
    val pips = 6

    if (debug) { 
      player1.setLogger(new ConsoleLogger("___PLAYER_1"))
      player2.setLogger(new ConsoleLogger("___PLAYER_2"))
      ui.setLogger(new ConsoleLogger("___UI"))
    }

    val game = new Dominoes(ui, player1, player2, goal, pips)

    val winner = game.play

    val winString =
      s"\n\n-- GAME OVER -- ${winner.getName} WINS WITH ${winner.getPoints} POINTS! --\n\n"
    println(winString)

  }


  /** Main game loop. Loops forever, starting a new game or quitting
    * only if certain exceptions are caught (as triggered by user action).
    */
  def mainGameLoop: Unit = { 
    while (true) { 
      breakable { 
        try { 
          runSingleGame
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

  mainGameLoop
}

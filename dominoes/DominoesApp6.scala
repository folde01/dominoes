package dominoes

import dominoes._
import dominoes.DominoesPrintUtil6._
import util.control.Breaks._

/** Object to run dominoes game. Sets up both players (with user input), the UI
  * and runs main game loop.
  */
object DominoesApp6 extends App { 


  /** This game logs debugging messages if debug is set to true 
    */
  private val debug = false


  /** Ask user for the number of points required to win and validate input.
    */
  private def askForGoal: Int = { 
    val entered = askUser("How many points should it take to win?")
    val regexIsInt = "^[0-9]+$"
    val n = 
      if (entered.matches(regexIsInt)) entered.toInt 
      else 0

    if (n > 0) n
    else { 
      printError("Invalid goal. Try again.")
      askForGoal
    }
  }


  /** Ask user if player being set up is human (as opposed to non-interactive).
    * and validate input.
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


  /** Ask user for name of player being set up and validate input. 
    * @param playerNum The player's number (1 or 2) 
    */ 
  private def askForPlayerName(playerNum: Int): String = { 
    val entered = askUser(s"Enter name of Player $playerNum:")
    entered match { 
      case "" => { 
        printError("The name can't be blank. Try again.")
        askForPlayerName(playerNum)
      }
      case _ => entered
    }

  }
  
  /** Run a single dominoes game
    */
  private def runSingleGame { 
    printAnnounce("Welcome to Dominoes!")
    printInfo("Instructions: At any prompt that follows, you can always type '..' to start a new game, 'qq' to quit, or 'h' for help remembering these instructions.")

    printInfo("Let's get started!")

    val name1 = askForPlayerName(1)
    var name2 = askForPlayerName(2)

    if (name2 == name1) { 
      while (name2 == name1) { 
        printError("Player 2's name can't be the same as Player 1's. Try again.")
        name2 = askForPlayerName(2)
      }
    }

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
      s"GAME OVER -- ${winner.getName} WINS WITH ${winner.getPoints} POINTS!"
    printAnnounce(winString)

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
            printAnnounce(". . . N E W   G A M E . . .")
            break  
          }
          case _: QuitException => { 
            printAnnounce("GOODBYE!")
            System.exit(0) 
          }
          case _: Throwable => System.exit(0) 
        }
      }
    }
  }

  mainGameLoop
}

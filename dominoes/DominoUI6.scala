package dominoes

import dominoes._
import dominoes.players._
import dominoes.DominoesUtil6._


/** A text-based user interface for the dominoes game. It has methods for updating the display at appropriate times.
  */
class DominoUI6 extends DominoUI { 


  /** Keeps track of current round
    */
  private var round = 1

    
  /** Shared CubbyHole
    */
  private var cubby: CubbyHole = null


  /** This method displays the current state of the players, the table, 
    * and the boneyard as appropriate for the UI.
    * @param players Both players
    * @param table The table containing the bone layout 
    * @param yard The yard of unused bones
    */
  def display(players: Array[DominoPlayer], table: Table, yard: BoneYard): Unit = { 
    log("display")
    displayTable(table)
    displayBoneYard(yard)
    displayStatus
    sendCubbyLayout

    def displayStatus = { 
      val p1 = players(0)
      val p2 = players(1)
      val scoreString = 
        s"\nSCORE ${p1.getName} ${p1.getPoints}, ${p2.getName} ${p2.getPoints} -- ROUND $round"
      println(scoreString)
    }

    def sendCubbyLayout: Unit = {
      log("sendCubbyLayout: " + bonesToString(table.layout))
      cubby.put(table.layout)
    }
  }

  
  /** This method displays that the specified player made an illegal play
    * @param player The player making the illegal play
    */
  def displayInvalidPlay(player: DominoPlayer): Unit = { 
    println
    println("  !!!!!!")
    println("  !!!!!!")
    println("  !!!!!! Invalid play by " + player.getName + ". Try again.")
    println("  !!!!!!")
    println("  !!!!!!\n")
  }


  /** This method displays the winner for the round
    * @param player The winning player for the round. null if there was a draw.
    */
  def displayRoundWinner(winner: DominoPlayer): Unit = { 
    log("displayRoundWinner") 
    
    val roundResult = 
      if (winner == null) "Round finished - it was a draw!"
      else winner.getName + " wins the round!"

    println
    println(".")
    println(" .")
    println("  .")
    println("   .")
    println("    .")
    println("     " + roundResult)
    println("    .")
    println("   .")
    println("  .")
    println(" .")
    println(".")

    round += 1
  }


  /** Set local reference to shared CubbyHole
    * @param c new reference 
    */
  def setCubby(c: CubbyHole) = cubby = c


  /** Display table layout
    * @param table The game's table
    */
  private def displayTable(table: Table): Unit = { 
    val layoutString = 
      if (table.layout.nonEmpty) { 
         table.layout.map(boneString).mkString(" ") 
      } else ""

    displayLayout(layoutString)

    def displayLayout(layout: String): Unit = { 
      println("\n  * * * *")
      println("  * * * *")
      println("  Layout:")
      println("  " + layout)
      println("  * * * *")
      println("  * * * *")
    }
  }


  /** Display yard of unused bones
    * @param yard current boneyard
    */
  private def displayBoneYard(yard: BoneYard): Unit = 
    println("\n  Boneyard size: " + yard.size) 


  /** Reference to a logger object (for debugging purposes)
    */
  var logger: Logger = null


  /** Set logger object so we can use its log method
    * @param logger The logger
    */
  def setLogger(logger: Logger): Unit = this.logger = logger 


  /** Log debug messages 
    * @param s message to log
    */
  private def log(s: String): Unit = 
    if (logger == null) {}
    else logger.log(s)

}


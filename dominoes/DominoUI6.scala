package dominoes

import dominoes._
import dominoes.players._
import dominoes.DominoesUtil6._

class DominoUI6 extends DominoUI { 

  var round = 1
  private var cubby: CubbyHole = null

  // for logging/debugging
  private val id = "USER__INTERFACE"
  private val debug = false
  //private val debug = true
  private val shouldLog = true
  private var counter = 0

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

  def displayInvalidPlay(player: DominoPlayer): Unit = { 
    println
    println("  !!!!!!")
    println("  !!!!!!")
    println("  !!!!!! Invalid play by " + player.getName + ". Try again.")
    println("  !!!!!!")
    println("  !!!!!!\n")
  }

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

  def setCubby(c: CubbyHole) = cubby = c

  private def displayHands(players: Array[DominoPlayer]): Unit = { 
    players.foreach { (player) =>
      val hand = player.bonesInHand.map(boneString).mkString(" ")
      println("\n  " + player.getName + "'s hand: " + hand)
    }
  }

  private def displayTable(table: Table): Unit = { 
    val layoutString = 
      if (table.layout.nonEmpty) { 
         table.layout.map(boneString).mkString(" ") 
      } else ""

    displayLayout(layoutString)
  }

  private def displayLayout(layout: String): Unit = { 
    println("\n  * * * *")
    println("  * * * *")
    println("  Layout:")
    println("  " + layout)
    println("  * * * *")
    println("  * * * *")
  }

  private def displayBoneYard(yard: BoneYard): Unit = 
    println("\n  Boneyard size: " + yard.size) 

  private def log(s: String) = {
    if (debug) {
      if (shouldLog)
        println(id + ":" + counter + ": " + s)
      counter += 1
    }
  }
  

}


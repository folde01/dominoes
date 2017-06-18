package dominoes

import dominoes._
import dominoes.players._

class DominoUI4 extends DominoUI { 

  var cubby: CubbyHole = null
  val id = "USER__INTERFACE"
  val debug = false
  //val debug = true
  val shouldLog = true
  var counter = 0
  var round = 2

  def setCubby(c: CubbyHole) = cubby = c

  def display(players: Array[DominoPlayer], table: Table, yard: BoneYard): Unit = { 
    log("display")


    displayTable(table)
    displayBoneYard(yard)
    displayScore
    //cubby.put(table.layout) 
    sendCubbyLayout

    def displayScore = { 
      val p1 = players(0)
      val p2 = players(1)
      val scoreString = 
        s"\n\n\n\n\nScore: ${p1.getName} ${p1.getPoints}, ${p2.getName} ${p2.getPoints}" 
      println(scoreString)
    }

    def sendCubbyLayout: Unit = {
      log("sendCubbyLayout: " + bonesToString(table.layout))
      cubby.put(table.layout)
    }
  }


  def bonesToString(bones: Seq[Bone]): String = {
      bones.map(boneString).mkString(" ")
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
  }

  def displayHands(players: Array[DominoPlayer]): Unit = { 
    players.foreach { (player) =>
      val hand = player.bonesInHand.map(boneString).mkString(" ")
      println("\n  " + player.getName + "'s hand: " + hand)
    }
  }

  def displayTable(table: Table): Unit = { 
    val layoutString = 
      if (table.layout.nonEmpty) { 
         table.layout.map(boneString).mkString(" ") 
      } else ""

    displayLayout(layoutString)
  }

  def displayLayout(layout: String): Unit = { 
    println("\n  * * * *")
    println("  * * * *")
    println("  Layout:")
    println("  " + layout)
    println("  * * * *")
    println("  * * * *")
  }

  def displayBoneYard(yard: BoneYard): Unit = 
    println("\n  Boneyard size: " + yard.size) 


  def boneString(bone: Bone): String = 
    s"${bone.left.toString}:${bone.right.toString}"


  def log(s: String) = {
    if (debug) {
      if (shouldLog)
        println(id + ":" + counter + ": " + s)
      counter += 1
    }
  }

}


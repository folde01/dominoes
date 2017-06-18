package dominoes

import dominoes._
import dominoes.players._

class DominoUI3 extends DominoUI { 

  var cubby: CubbyHole = null
  val id = "USER__INTERFACE"
  //val debug = false
  val debug = true
  val shouldLog = true
  var counter = 0

  def setCubby(c: CubbyHole) = cubby = c

  def display(players: Array[DominoPlayer],table: Table, yard: BoneYard): Unit = { 
    log("display")

    def sendCubbyLayout: Unit = {
      log("sendCubbyLayout: " + bonesToString(table.layout))
      cubby.put(table.layout)
    }

    displayTable(table)
    displayBoneYard(yard)
    //cubby.put(table.layout) 
    sendCubbyLayout
  }

  def bonesToString(bones: Seq[Bone]): String = {
      bones.map(boneString).mkString(" ")
  }
  
  def displayInvalidPlay(x$1: dominoes.players.DominoPlayer): Unit = println("displayInvalidPlay")
  def displayRoundWinner(x$1: dominoes.players.DominoPlayer): Unit = println("displayRoundWinner") 
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


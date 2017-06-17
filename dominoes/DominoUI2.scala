package dominoes

import dominoes._
import dominoes.players._

class DominoUI2 extends DominoUI { 

  def display(players: Array[DominoPlayer],table: Table, yard: BoneYard): Unit = { 
    //println("---")
    displayTable(table)
    displayBoneYard(yard)
    //displayHands(players)
    //println("---")
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

}


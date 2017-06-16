package dominoes

import dominoes._
import dominoes.players._

class DominoUI2 extends DominoUI { 

  def display(players: Array[DominoPlayer],table: Table, yard: BoneYard): Unit = { 
    println("---")
    displayTable(table)
    displayBoneYard(yard)
    displayHands(players)
    println("---")
  }

  def displayInvalidPlay(x$1: dominoes.players.DominoPlayer): Unit = println("displayInvalidPlay")
  def displayRoundWinner(x$1: dominoes.players.DominoPlayer): Unit = println("displayRoundWinner") 
  def displayHands(players: Array[DominoPlayer]): Unit = { 
    players.foreach { (player) =>
      val hand = player.bonesInHand.map(boneString).mkString(" ")
      println(player.getName + "'s hand: " + hand)
    }
  }

  def displayTable(table: Table): Unit = { 
    val (leftString, rightString, layoutString) = 
      if (table.layout.nonEmpty) { 
        (table.left.toString, table.right.toString,
         table.layout.map(boneString).mkString(" ")) 
      } 
      else ("", "", "")

    println("Left: " + leftString)
    println("Right: " + rightString)
    println("Layout: " + layoutString)
  }

  def displayBoneYard(yard: BoneYard): Unit = 
    println("Boneyard size: " + yard.size) 


  def boneString(bone: Bone): String = 
    s"${bone.left.toString}:${bone.right.toString}"

}


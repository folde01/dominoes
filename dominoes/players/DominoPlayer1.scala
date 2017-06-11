package dominoes.players

import dominoes._
import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

class DominoPlayer1 extends DominoPlayer { 

  var bones = Array[Bone]()
  def getBones = bones

  def bonesInHand(): Array[dominoes.Bone] = ???
  def draw(yard: dominoes.BoneYard): Unit = { 
    val bone = yard.draw()
    if (bone != null) 
      bones = bones :+ bone 
  }
  def getName(): String = ???
  def getPoints(): Int = ???
  def makePlay(x$1: dominoes.Table): dominoes.Play = ???
  def newRound(): Unit = ???
  def numInHand(): Int = bones.length 
  def setName(x$1: String): Unit = ???
  def setPoints(x$1: Int): Unit = ???
  def takeBack(x$1: dominoes.Bone): Unit = ???
}

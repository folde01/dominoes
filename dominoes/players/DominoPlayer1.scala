package dominoes.players

import dominoes._
import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

class DominoPlayer1 extends DominoPlayer { 

  var name: String = "not set" 
  var points = 0 
  var bones = Array[Bone]()
  //def getBones = bones
  //var playChooser: PlayChooser = null

  def bonesInHand(): Array[Bone] = bones 

  def draw(yard: BoneYard): Unit = { 
    val bone = yard.draw()
    if (bone != null) 
      bones = bones :+ bone 
  }

  def getName(): String = name 

  def getPoints(): Int = points

  def makePlay(table: Table): Play = {
    val play = choosePlay
    val bone = play.bone
    val playString = s"${bone.left}:${bone.right} at ${play.end}"

    try { 
      //output(s"Attempting: $playString") 
      table.play(play)
      //output(s"Accepted: $playString") 
    } catch { 
      case e: InvalidPlayException => { 
        takeBack(bone)
        makePlay(table)
      }
    }
  }

  def newRound(): Unit = bones = Array[Bone]()

  def numInHand(): Int = bones.length 

  def setName(n: String): Unit = name = n 

  def setPoints(newScore: Int): Unit = points = newScore 

  def takeBack(bone: Bone): Unit = { 
    val s = "Rejected: ${bone.left}:${bone.right}"
    output(s)
  }

  def choosePlay: Play = { 
    val r = scala.util.Random
    val bone = bones(r.nextInt(bones.size)) 

    if (r.nextInt(2) == 1) 
      bone.flip 

    val end = { 
      if (r.nextInt(2) == 1) Play.RIGHT
      else Play.LEFT
    }

    new Play(bone, end)
  }

  //def setPlayChooser(pc: PlayChooser): Unit = playChooser = pc 

  def output(s: String) = println(s)

}

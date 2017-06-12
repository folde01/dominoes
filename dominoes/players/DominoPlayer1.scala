package dominoes.players

import dominoes._

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

/*
  // we get a stack overflow if we do it this way and play them against each other
  // so do it non-recursively
  def makePlay(table: Table): Play = {
    val play: Play = choosePlay
    val bone = play.bone()
    //val playString = s"${bone.left}:${bone.right} at ${play.end}"
    //val playString = bone.left + ":" + bone.right + " at " + play.end
    val playString = "FOO"

    try { 
      output(s"Attempting: $playString") 
      output(s"Accepted: $playString") 
      table.play(play)
      play
    } 
    catch { 
      case e: InvalidPlayException => { 
        takeBack(bone)
        makePlay(table)
        //play
      }
    }  
  }
  */

  def makePlay(table: Table): Play = {

    var playNotMade = true 
    var play: Play = null

    while (playNotMade) { 
      play = choosePlay
      val bone = play.bone()
      val playString = bone.left + ":" + bone.right + " at " + play.end
      output(s"Attempting: $playString") 
      try { 
        //val playString = s"${bone.left}:${bone.right} at ${play.end}"
        table.play(play)
        output(s"Accepted: $playString ##########################") 
        playNotMade = false
      } 
      catch { 
        case e: InvalidPlayException => { 
          takeBack(bone)
        }
      }  
    }
    play
  }


  def newRound(): Unit = bones = Array[Bone]()

  def numInHand(): Int = bones.length 

  def setName(n: String): Unit = name = n 

  def setPoints(newScore: Int): Unit = points = newScore 

  def takeBack(bone: Bone): Unit = { 
    val playString = bone.left + ":" + bone.right
    val s = "Rejected: " + playString
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

    val p = new Play(bone, end)
    //println(p.bone.left)
    p
  }

  //def setPlayChooser(pc: PlayChooser): Unit = playChooser = pc 

  def output(s: String) = println(s)

}

package dominoes.players

import dominoes._

class DominoPlayer1 extends DominoPlayer { 

  val id = scala.util.Random.nextInt(100000)

  var counter = 0  

  var shouldLog = true
  var name: String = "not set" 
  var points = 0 
  var bones = Array[Bone]()
  //def getBones = bones
  //var playChooser: PlayChooser = null

  def bonesInHand(): Array[Bone] = { 
    log("bonesInHand: " + bones)
    bones 
  }

  def draw(yard: BoneYard): Unit = { 
    val bone = yard.draw()
    if (bone != null) 
      bones = bones :+ bone 
    log("draw: " + bone.left + ":" + bone.right) 
  }

  def getName(): String = name 

  def getPoints(): Int = { 
    log("getPoints: " + points)
    points
  }

  def makePlay(table: Table): Play = {
    val play = choosePlay
    val bone = play.bone()
    val playString = bone.left + ":" + bone.right + " at " + play.end
    log(s"makePlay attempting: $playString") 
    val newbones = bones.filter(_ != bone)
    bones = newbones
    table.play(play)
    Thread sleep 1000
    log(s"Accepted: $playString") 
    play
  }

  def newRound(): Unit = { 
    log("new round")
    bones = Array[Bone]()
  }

  def numInHand(): Int = { 
    log("numInHand: " + bones.length)
    bones.length 
  }

  def setName(n: String): Unit = name = n 

  def setPoints(newScore: Int): Unit = { 
    points = newScore 
    log("setPoints:" + newScore)
  }

  def takeBack(bone: Bone): Unit = { 
    bones :+ bone
    log("takeBack: " + bone.left + ":" + bone.right)
  }

  def choosePlay: Play = { 
    log("choosePlay starting")
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
    Thread sleep 1000
    p
  }

  //def setPlayChooser(pc: PlayChooser): Unit = playChooser = pc 

  def output(s: String) = println(s)

  def log(s: String) = { 
    if (shouldLog)
      println(id + ":" + counter + ": " + s)
    counter += 1
  }

}

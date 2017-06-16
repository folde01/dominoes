package dominoes.players

import dominoes._

class DominoPlayer2 extends DominoPlayer { 

  val id = scala.util.Random.nextInt(100000)

  var counter = 0  

  var shouldLog = true
  var name: String = "not set" 
  var points = 0 
  var bones = Array[Bone]()
  //def getBones = bones
  //var playChooser: PlayChooser = null

  def bonesInHand(): Array[Bone] = { 
    //log("bonesInHand: " + bones)
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
    println(getName + "'s turn. Your hand: ")

    
    val bone = new Bone( 

    print("Enter the side to play it on: ")
    val endEntered = scala.io.StdIn.readLine()
    
    val play = new Play(bone, end)
    val playString = bone.left + ":" + bone.right + " at " + play.end
    log(s"makePlay attempting: $playString") 
    val newbones = bones.filter(_ != bone)
    bones = newbones
    table.play(play)
    //Thread sleep 1000
    log(s"Accepted: $playString") 
    play
  }

  def chooseBone: String = { 
    print("Enter a bone to play: ")
    val boneEntered = scala.io.StdIn.readLine()
    if (validBone(boneEntered)) { 
    }
  }

  def validBone(s: String): Boolean = { 
    val rgx = "^\\s*[0-9]+:[0-9]+\\s*$"
    if (!s.matches(rgx) || bones != null) 
      false
    else { 
      val bone = new Bone(s.split(":")(0).toInt, s.split(":")(1).toInt)
      bones.contains(bone)
    }
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
    log("setPoints:" + newScore)
    points = newScore 
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
    //Thread sleep 1000
    p
  }

  //def setPlayChooser(pc: PlayChooser): Unit = playChooser = pc 

  def output(s: String) = println(s)

  def log(s: String) = { 
    if (shouldLog)
      //println(id + ":" + counter + ": " + s + " -- numInHand: " + numInHand())
      println(id + ":" + counter + ": " + s)
    counter += 1
  }

}

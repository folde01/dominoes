package dominoes.players

import dominoes._

class DominoPlayer2 extends DominoPlayer { 

  val id = scala.util.Random.nextInt(100000)

  var counter = 0  
  val debug = false
  //val debug = true

  var shouldLog = true
  var name: String = "not set" 
  var points = 0 
  var bones = Array[Bone]()
  //def getBones = bones
  //var playChooser: PlayChooser = null

  def bonesInHand(): Array[Bone] = { 
    log("bonesInHand: " + bonesToString(bones))
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
    log("makePlay")
    displayHand

    val boneOption = requestBone

    boneOption match { 
      case None => 
        throw new CantPlayException("No bone received from requestBone.")
      case _ => { 
        val bone = boneOption.get
        val end = requestEnd
        val play = new Play(bone, end)
        val left = bone.left
        val right = bone.right
        val playString = left + ":" + right + " at " + play.end
        println("Playing: " + playString)
        val newbones = bones.filter(b => (!b.equals(bone)))
        log("newbones: " + bonesToString(newbones))
        bones = newbones
        play
      }
    }
  }

  def requestBone: Option[Bone] = { 
    log("requestBone")
    // todo: how do we end current game?

    val cantPlay = "cp"

    print(s"\nEnter bone to play (eg '0:1') or '$cantPlay' if you can't play: ")
    val entered = scala.io.StdIn.readLine().trim

    if (entered == cantPlay) { 
      if (canPlayFromHand) { 
        println("You have a bone you can play. Try again.")
        requestBone
      } else { 
        None
      }
    } else { 
      if (isValidBoneString(entered)) { 
        val boneOption = makeBoneOptionFromString(entered)
        val bone = boneOption.get
        println("You entered " + bone.left + ":" + bone.right) 
        if (handHasBone(bone)) { 
          boneOption
        } else { 
          println("That's not in your hand. Try again.")
          requestBone
        }
      } else { 
        println("That's not a bone. Try again.")
        requestBone
      }
    }
  }

  // todo: implement
  def canPlayFromHand = { 
    log("canPlayFromHand")
    true 
  }

  def requestEnd: Int = { 
    log("requestEnd")
    println("Enter the end to play on (0 for left, 1 for right): ")
    val entered = scala.io.StdIn.readLine().trim

    entered match { 
      case "0" => Play.LEFT 
      case "1" => Play.RIGHT 
      case _ => { 
        println("Not a valid end. Try again.")
        requestEnd 
      }
    }
  }
     
  def isValidBoneString(s: String): Boolean = { 
    log("isValidBoneString")
    val rgx = "^\\s*[0-9]+:[0-9]+\\s*$"
    s.matches(rgx)
  }

  def makeBoneOptionFromString(s: String): Option[Bone] = { 
    log("makeBoneFromString")
    if (isValidBoneString(s)) 
      Some(new Bone(s.split(":")(0).toInt, s.split(":")(1).toInt))
    else
      None
  }

  def handHasBone(b: Bone) = bones.exists(_.equals(b))

  def displayHand: Unit = { 
    log("displayHand")
    println("\n#####   " + getName + "'s turn" + "   #####")
    if (numInHand == 0) { 
      println("\nYour hand is empty.")
    }
    else { 
      println("\nYour hand: " + bonesToString(bones))
    }
  }

  def boneString(bone: Bone): String =
    s"${bone.left.toString}:${bone.right.toString}"

  def bonesToString(bones: Seq[Bone]): String = { 
      bones.map(boneString).mkString(" ")
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
    log("takeBack: " + bone.left + ":" + bone.right)
    bones :+ bone
  }

  def log(s: String) = { 
    if (debug) { 
      if (shouldLog)
        println(id + ":" + counter + ": " + s)
      counter += 1
    }
  }

}

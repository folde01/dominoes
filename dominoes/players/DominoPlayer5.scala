package dominoes.players

import dominoes._

class DominoPlayer5(cubby: CubbyHole, human: Boolean) extends DominoPlayer { 

  val id = "PLAYER__" + scala.util.Random.nextInt(100000)

  var counter = 0  
  val debug = false
  //val debug = true

  // from CubbyHole
  var cubbyLayout = Array[Bone]()
  
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
    //println("\n  D R A W . . . " + getName + " drew " + boneString(bone))
    if (bone != null) 
      bones = bones :+ bone 
    log("draw: " + bone.left + ":" + bone.right) 
  }

  def getName(): String = name 

  def getPoints(): Int = { 
    log("getPoints: " + points)
    points
  }

  var newTurn = true


  def makePlay(table: Table): Play = {
    log("makePlay")

    displayHand

    def pullLayoutFromCubby = { 
        cubbyLayout = cubby.get.asInstanceOf[Array[Bone]]
        log("pullCubbyLayout: " + bonesToString(cubbyLayout))
    }

    def pushLayoutToCubby = { 
        log("pushCubbyLayout: " + bonesToString(cubbyLayout))
        cubby.put(cubbyLayout)
    }

    pullLayoutFromCubby

    if (human) { 
      val boneOption = requestBone

      boneOption match { 
        case None => 
          println("\n  <<<<<< " + getName + " has no bone to play, so draws if possible >>>>>>")
          pushLayoutToCubby
          throw new CantPlayException("No bone received from requestBone.")
        case _ => { 
          val bone = boneOption.get
          val end = requestEnd
          val play = new Play(bone, end)
          val left = bone.left
          val right = bone.right
          val playString = left + ":" + right + " on the " + 
            { if (end == 0) "left" else "right" }
          println("\n  <<<<<< " + getName + " playing " + playString + " >>>>>>")
          val newbones = bones.filter(b => (!b.equals(bone)))
          log("newbones: " + bonesToString(newbones))
          bones = newbones
          pushLayoutToCubby
          play
        }
      }
    } else { 
      val playOption = choosePlay
      
      playOption match { 
        case None => 
          pushLayoutToCubby
          println("\n  <<<<<< " + getName + " has no bone to play, so draws if possible >>>>>>")
          throw new CantPlayException("No play received from choosePlay.")
        case Some(play) => { 
          val left = play.bone.left
          val right = play.bone.right
          val playString = left + ":" + right + " on the " + 
            { if (play.end == 0) "left" else "right" }
          println("\n  <<<<<< " + getName + " playing " + playString + " >>>>>>")
          val newbones = bones.filter(b => (!b.equals(play.bone)))
          log("newbones: " + bonesToString(newbones))
          bones = newbones
          pushLayoutToCubby
          play
        }
      }
    }
  }

  def getCubbyLayout = cubbyLayout

  def canPlayFromHand = { 
    log("canPlayFromHand")
    //val layout = cubby.get.asInstanceOf[Array[Bone]]
    //val layout = cubbyLayout
    val layout = getCubbyLayout
    log("Layout from cubby: " + bonesToString(layout))

    // check each in hand to see if it has left or right
    val leftEnd = layout.head.left
    val rightEnd = layout.last.right
    def hasInt(b: Bone, n: Int) = b.left == n || b.right == n
    bones.exists(hasInt(_,leftEnd)) || bones.exists(hasInt(_,rightEnd))
  }

  def choosePlay: Option[Play] = { 
    log("choosePlay")
    //val layout = cubby.get.asInstanceOf[Array[Bone]]
    val layout = getCubbyLayout
    log("Layout from cubby: " + bonesToString(layout))

    // check each in hand to see if it has left or right
    val leftEnd = layout.head.left
    val rightEnd = layout.last.right
    log("leftEnd: " + leftEnd + ", rightEnd: " + rightEnd) 

    def hasInt(b: Bone, n: Int) = b.left == n || b.right == n

    val boneOptionMatchingLeftEnd = bones.find(hasInt(_,leftEnd))
    val boneOptionMatchingRightEnd = bones.find(hasInt(_,rightEnd))

    boneOptionMatchingLeftEnd match { 
      case Some(bone) => Some(new Play(bone, Play.LEFT))
      case None => { 
        boneOptionMatchingRightEnd match { 
          case Some(bn) => Some(new Play(bn, Play.RIGHT))
          case None => None
        }
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


  def requestEnd: Int = { 
    log("requestEnd")
    print("Enter the end to play on (0 for left, 1 for right): ")
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
    val newbones = bones :+ bone
    bones = newbones
    // todo: put back so hand is in same order as before and not flipped
  }

  def log(s: String) = { 
    if (debug) { 
      if (shouldLog)
        println(id + ":" + counter + ": " + s)
      counter += 1
    }
  }

}

package dominoes.players

import dominoes._
import dominoes.DominoesUtil6._

class DominoPlayer6(cubby: CubbyHole, human: Boolean) extends DominoPlayer { 

  private var cubbyLayout = Array[Bone]()
  private var name: String = "" 
  private var points = 0 
  private var bones = Array[Bone]()

  // for debugging: change debug to true to see logging done by log method
  private val debug = false
  private var shouldLog = true
  private var counter = 0  
  private val id = "PLAYER__" + scala.util.Random.nextInt(100000)

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


  def makePlay(table: Table): Play = {
    log("makePlay")
    displayHand
    pullLayoutFromCubby

    if (human) { 
      val boneOption = requestBone

      boneOption match { 
        case None => 
          announceMove(s"$getName has no bone to play")
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
          announceMove(s"$getName playing $playString")
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
          announceMove(s"$getName has no bone to play")
          throw new CantPlayException("No play received from choosePlay.")
        case Some(play) => { 
          val left = play.bone.left
          val right = play.bone.right
          val playString = left + ":" + right + " on the " + 
            { if (play.end == 0) "left" else "right" }
          val newbones = bones.filter(b => (!b.equals(play.bone)))
          log("newbones: " + bonesToString(newbones))
          bones = newbones
          pushLayoutToCubby
          play
        }
      }
    }
  }

  private def announceMove(move: String) = println(s"\n  <<<<<< $move >>>>>>")

  private def getCubbyLayout = cubbyLayout

  private def canPlayFromHand = { 
    log("canPlayFromHand")
    val layout = getCubbyLayout
    log("Layout from cubby: " + bonesToString(layout))
    val leftEnd = layout.head.left
    val rightEnd = layout.last.right
    def hasInt(b: Bone, n: Int) = b.left == n || b.right == n
    bones.exists(hasInt(_,leftEnd)) || bones.exists(hasInt(_,rightEnd))
  }

  private def choosePlay: Option[Play] = { 
    log("choosePlay")
    val layout = getCubbyLayout
    log("Layout from cubby: " + bonesToString(layout))
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

  private def requestBone: Option[Bone] = { 
    log("requestBone")
    val cantPlay = "cp"
    val prompt = s"Enter bone to play (eg '0:1') or '$cantPlay' if you can't play:"
    val entered = askUser(prompt)
    
    if (entered == cantPlay) { 
      if (canPlayFromHand) { 
        println("\nYou have a bone you can play. Try again.")
        requestBone
      } else { 
        None
      }
    } else { 
      if (isValidBoneString(entered)) { 
        val boneOption = makeBoneOptionFromString(entered)
        val bone = boneOption.get
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


  private def requestEnd: Int = { 
    log("requestEnd")
    val prompt = "Enter the end to play on (0 for left, 1 for right):"
    val entered = askUser(prompt) 

    entered match { 
      case "0" => Play.LEFT 
      case "1" => Play.RIGHT 
      case _ => { 
        println("Not a valid end. Try again.")
        requestEnd 
      }
    }
  }
     
  private def isValidBoneString(s: String): Boolean = { 
    log("isValidBoneString")
    val rgx = "^\\s*[0-9]+:[0-9]+\\s*$"
    s.matches(rgx)
  }

  private def makeBoneOptionFromString(s: String): Option[Bone] = { 
    log("makeBoneFromString")
    if (isValidBoneString(s)) 
      Some(new Bone(s.split(":")(0).toInt, s.split(":")(1).toInt))
    else
      None
  }

  private def handHasBone(b: Bone) = bones.exists(_.equals(b))

  private def log(s: String) = { 
    if (debug) { 
      if (shouldLog)
        println(id + ":" + counter + ": " + s)
      counter += 1
    }
  }

  private def pullLayoutFromCubby = { 
      cubbyLayout = cubby.get.asInstanceOf[Array[Bone]]
      log("pullCubbyLayout: " + bonesToString(cubbyLayout))
  }

  private def pushLayoutToCubby = { 
      log("pushCubbyLayout: " + bonesToString(cubbyLayout))
      cubby.put(cubbyLayout)
  }

  private def displayHand: Unit = { 
    log("displayHand")
    println("\n#####   " + getName + "'s turn" + "   #####")
    if (numInHand == 0) { 
      println("\nYour hand is empty.")
    }
    else { 
      println("\nYour hand: " + bonesToString(bones))
    }
  }
}

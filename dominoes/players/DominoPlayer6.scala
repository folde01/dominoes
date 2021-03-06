package dominoes.players

import dominoes._
import dominoes.DominoesPrintUtil6._

/** A class to represent a dominoes player.
  * @constructor Create a new player 
  * @param cubby A CubbyHole object for sharing the current layout on the table 
  * @param human Whether the player is human (otherwise it's non-interactive).
  * @param debug Whether debugging messages are needed. 
  */
case class DominoPlayer6(private val cubby: CubbyHole, private val human: Boolean, private val debug: Boolean = false) extends DominoPlayer { 

  /** Stores the table layout to be passed back and forth between players via the UI
    */
  private var cubbyLayout = Array[Bone]()


  /** Player's name, as set by setName
    */
  private var name: String = "" 


  /** Player's score, as set by setPoints
    */
  private var points = 0 


  /** Player's bones (tiles) - changes by drawing and making/taking back plays.
    */
  private var bones = Array[Bone]()


  /** About to start a new round. Clear out the player's hand.
    */
  def newRound(): Unit = { 
    log("new round")
    bones = Array[Bone]()
  }


  /** The number of bones the player has in its hand
    * @return how many bones the player has
    */
  def numInHand(): Int = { 
    log("numInHand: " + bones.length)
    bones.length 
  }


  /** Set the player's name
    * @param name The players name.
    */
  def setName(name: String): Unit = this.name = name


  /** Set the number of points the player has
    * @param newScore Player's new score
    */
  def setPoints(newScore: Int): Unit = { 
    log("setPoints:" + newScore)
    points = newScore 
  }


  /** Tell the player to take back the specified bone.
    * This usually happens when the player has tried to play an invalid bone.
    * @param bone The bone to take back
    * @todo Put back so hand is in same order as before and bone isn't flipped
    */
  def takeBack(bone: Bone): Unit = { 
    log("takeBack: " + bone.left + ":" + bone.right)
    val newbones = bones :+ bone
    bones = newbones
  }


  /** The bones in the player's hand.
    * @return Array of bones belonging to player.
    */
  def bonesInHand(): Array[Bone] = { 
    log("bonesInHand: " + bonesToString(bones))
    bones 
  }


  /** Draw a tile from the given boneyard.
    * @param yard The boneyard, used by both players.
    */
  def draw(yard: BoneYard): Unit = { 
    val bone = yard.draw()
    if (bone != null) 
      bones = bones :+ bone 
    log("draw: " + bone.left + ":" + bone.right) 
  }


  /** Return the name of the player.
    * @return Player's name
    */
  def getName(): String = name 



  /** Get the number of points the player has.
    * @return Player's current score.
    */
  def getPoints(): Int = { 
    log("getPoints: " + points)
    points
  }


  /** Pick a bone to play and where it should be played.
    * For interactive players, it asks the user these two questions. The user can also indicate that they have no bone to play - in which case if they actually do have a bone to play, they are told this and then asked again for their move.
    * For non-interactive players, it chooses the first valid play it can find.
    * @param table The table used by both players in the game
    * @return The play made by the player
    * @throws CantPlayException if no play can be made using bones in hand 
    */
  @throws(classOf[CantPlayException])
  def makePlay(table: Table): Play = {
    log("makePlay")
    displayHand
    pullLayoutFromCubby

    if (human) { 
      val boneOption = requestBone
      boneOption match { 
        case None => 
          announceMove(None)
          pushLayoutToCubby
          throw new CantPlayException("No bone received from requestBone.")
        case _ => { 
          val bone = boneOption.get
          val end = requestEnd
          val play = new Play(bone, end)
          announceMove(Some(play))
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
          announceMove(None)
          throw new CantPlayException("No play received from choosePlay.")
        case Some(play) => { 
          announceMove(Some(play))
          val newbones = bones.filter(b => (!b.equals(play.bone)))
          log("newbones: " + bonesToString(newbones))
          bones = newbones
          pushLayoutToCubby
          play
        }
      }
    }
  }
 

  /** Announce the player's move 
    * @param play The play to announce (if one could be made).
    */
  private def announceMove(playOption: Option[Play]): Unit = { 
    val playString = playOption match { 
      case Some(play) => { 
        val left = play.bone.left
        val right = play.bone.right
        val end = play.end
        s"$getName playing ${left}:${right} on the " + 
          { if (end == 0) "left" else "right" }
      }
      case None => s"$getName has no bone to play"
    }
    printPlay(playString)
  }

  
  /** Get the current layout retrieved from CubbyHole.
    @return current layout
    */
  private def getCubbyLayout = cubbyLayout


  /** Check whether player has a valid bone to play (ie no need to draw).
    * @return whether player has a valid bone to play
    */
  private def canPlayFromHand: Boolean = { 
    log("canPlayFromHand")
    val layout = getCubbyLayout
    log("Layout from cubby: " + bonesToString(layout))
    val leftEnd = layout.head.left
    val rightEnd = layout.last.right
    def hasInt(b: Bone, n: Int) = b.left == n || b.right == n
    bones.exists(hasInt(_,leftEnd)) || bones.exists(hasInt(_,rightEnd))
  }


  /** Used by non-interactive player to choose a play.
    * @return play to make (if any can be made) 
    */ 
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


  /** Ask user which bone to play and give "can't play" option, but check if it
    * it's properly being used. Validate whether bone chosen is a valid bone, a bone in 
    * the player's hand, and that it can be played. Ask again as needed. 
    * @return bone chosen (if there's one which can be played)
    */
  private def requestBone: Option[Bone] = { 
    log("requestBone")
    val cantPlay = "cp"
    val prompt = s"Enter bone to play (eg '0:1') or '$cantPlay' if you can't play:"
    val entered = askUser(prompt)
    
    if (entered == cantPlay) { 
      if (canPlayFromHand) { 
        printError("You have a bone you can play. Try again.")
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
          printError("That's not in your hand. Try again.")
          requestBone
        }
      } else { 
        printError("That's not a bone. Try again.")
        requestBone
      }
    }
  }


  /** Ask user for end to play chosen bone on. Validate and ask again as needed.
    * @return left or right end of the layout
    */ 
  private def requestEnd: Int = { 
    log("requestEnd")
    val prompt = "Enter the end to play on (0 for left, 1 for right):"
    val entered = askUser(prompt) 

    entered match { 
      case "0" => Play.LEFT 
      case "1" => Play.RIGHT 
      case _ => { 
        printError("Not a valid end. Try again.")
        requestEnd 
      }
    }
  }
     
  
  /** Check whether a string (probably entered by a user) is a valid representation
    * of a bone, ie in the format '3:4' for a bone with a 3 and a 4.
    * @param string to check
    * @return whether string is a valid bone string
    */
  private def isValidBoneString(s: String): Boolean = { 
    log("isValidBoneString")
    val rgx = "^\\s*[0-9]+:[0-9]+\\s*$"
    s.matches(rgx)
  }


  /** Check that input is a valid bone representation and if so return
    * corresponding bone 
    * @param string to check and convert
    * @return Some(bone) or None
    */
  private def makeBoneOptionFromString(s: String): Option[Bone] = { 
    log("makeBoneFromString")
    if (isValidBoneString(s)) 
      Some(new Bone(s.split(":")(0).toInt, s.split(":")(1).toInt))
    else
      None
  }


  /** Check if player has a given bone
    * @param bone to check for
    * @return whether player has the bone
    */
  private def handHasBone(b: Bone): Boolean = bones.exists(_.equals(b))


  /** Retrieve table layout from shared CubbyHole.
    */
  private def pullLayoutFromCubby: Unit = { 
      cubbyLayout = cubby.get.asInstanceOf[Array[Bone]]
      log("pullCubbyLayout: " + bonesToString(cubbyLayout))
  }


  /** Store table layout in shared CubbyHole.
    */
  private def pushLayoutToCubby: Unit = { 
      log("pushCubbyLayout: " + bonesToString(cubbyLayout))
      cubby.put(cubbyLayout)
  }


  /** Display player's hand.
    */
  private def displayHand: Unit = { 
    log("displayHand")
    printNewTurn(s"${getName}'s turn")
    if (numInHand == 0) { 
      printInfo("Your hand is empty.")
    }
    else { 
      printInfo("Your hand: " + bonesToString(bones))
    }
  }


  /** Reference to a logger object (for debugging purposes)
    */
  var logger: Logger = null


  /** Set logger object so we can use its log method
    * @param logger The logger
    */
  def setLogger(logger: Logger): Unit = this.logger = logger


  /** Log debug messages
    * @param s message to log
    */
  private def log(s: String): Unit =
    if (logger == null) {}
    else logger.log(s)

}

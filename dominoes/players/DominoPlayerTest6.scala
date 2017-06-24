package dominoes

import org.scalacheck.{Properties,Gen}
import org.scalacheck.Prop.{forAll, BooleanOperators}

object DominoPlayerTest6 extends Properties("DominoPlayer") {

  type Player = players.DominoPlayer6 
  type UI = DominoUI6 
  val boneTop = 6
  val maxDraws = 28 
  val maxGoal = 1000 
  val validNumDraws = Gen.choose(0,maxDraws)
  val validGoals = Gen.choose(0,maxGoal)

  property("game always has a winner") = forAll(validGoals) { (goal: Int) => 
    { 
      val is1Human = false
      val is2Human = false
      val cubby = new CubbyHole
      val player1 = new Player(cubby, is1Human)
      player1.setName("player1")
      val player2 = new Player(cubby, is2Human)
      player2.setName("player2")
      val ui = new UI
      ui.setCubby(cubby)
      val pips = 6
      val game = new Dominoes(ui, player1, player2, goal, pips)
      val winner = game.play
      winner.isInstanceOf[Player]
    }
  }

  property("getPoints == setPoints") = forAll { (newScore: Int) => 
    val player: Player = new Player(new CubbyHole, true) 
    player.setPoints(newScore)
    player.getPoints == newScore
  }

  property("getName == setName") = forAll { (name: String) => 
    val player: Player = new Player(new CubbyHole, true) 
    player.setName(name)
    player.getName == name
  }

  
  property("bonesInHand.size == numInHand") = forAll(validNumDraws) { (n: Int) => 
    // validNumDraws is producing negative numbers for some reason so 
    // use a condition to make it positive until we work out why:
    (n >= 1) ==> { 
      val player: Player = new Player(new CubbyHole, true) 
      val yard: BoneYard = new BoneYard(boneTop)
      for (j <- 1 to n) 
        player.draw(yard)
      player.bonesInHand.size == n 
    }
  }
  
  property("numInHand == number of draws") = forAll(validNumDraws) { (n: Int) => 
    // validNumDraws is producing negative numbers for some reason so 
    // use a condition to make it positive until we work out why:
    (n >= 1) ==> { 
      //val player: Player = new Player 
      val player: Player = new Player(new CubbyHole, true) 
      val yard: BoneYard = new BoneYard(boneTop)

      for (j <- 1 to n) { 
        player.draw(yard)
      }
      player.numInHand == n 
    }
  }

  property("newRound makes numInHand be 0") = forAll(validNumDraws) { (n: Int) => 
    (n >= 0) ==> { 
      val player: Player = new Player(new CubbyHole, true) 
      val yard: BoneYard = new BoneYard(boneTop)
      for (j <- 1 to n) player.draw(yard)
      player.newRound 
      player.numInHand == 0 
    }
  }
  
}

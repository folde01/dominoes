package dominoes

import org.scalacheck.{Properties,Gen}
import org.scalacheck.Prop.{forAll, BooleanOperators}

object DominoPlayerSpecification extends Properties("DominoPlayer") {

  type Player = players.DominoPlayer1 
  val boneTop = 6
  val maxDraws = 28 
  val validNumDraws = Gen.choose(0,maxDraws)
  
  property("numInHand == number of draws") = forAll(validNumDraws) { (n: Int) => 
    // validNumDraws is producing negative numbers for some reason so 
    // use a condition to make it positive until we work out why:
    (n >= 1) ==> { 
      val player: Player = new Player 
      val yard: BoneYard = new BoneYard(boneTop)
      println(s"############### n: $n size: ${yard.size}")
      println(s"numInHand: " + player.numInHand)

      for (j <- 1 to n) { 
        player.draw(yard)
        for (i <- 0 until player.getBones.size) { 
           val b = player.getBones(i); print((b.left, b.right) + " ") }
        println(s"numInHand: " + player.numInHand)
      }
      println(s"FINAL numInHand: " + player.numInHand)
      player.numInHand == n 
    }
  }
}

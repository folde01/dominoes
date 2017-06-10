package dominoes

import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

object DominoPlayerSpecification extends Properties("DominoPlayer") {

  type Player = players.DominoPlayer1 
  
  property("bonesInHand") = forAll { (n: Int) => 
    //val player: Player = new Player 
    val table: Table = new Table 
    n == n
  }

}

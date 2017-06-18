package dominoes

import dominoes._

class DominoUI1 extends DominoUI { 
  def display(x$1: Array[dominoes.players.DominoPlayer],x$2: dominoes.Table,x$3: dominoes.BoneYard): Unit = println("display") 
  def displayInvalidPlay(x$1: dominoes.players.DominoPlayer): Unit = println("displayInvalidPlay")
  def displayRoundWinner(x$1: dominoes.players.DominoPlayer): Unit = println("displayRoundWinner") 
}


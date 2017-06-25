package dominoes

import dominoes._

object DominoesPrintUtil6 { 

  def askUser(prompt: String): String = {
    printRequest(prompt)
    val entered = scala.io.StdIn.readLine().trim

    entered match {
      case "h" => { 
        val helpString = s"At any prompt, you can also type '..' to start a new game, or 'qq' to quit."
        printInfo(helpString)
        askUser(prompt)
      }
      case ".." => throw new NewGameException
      case "qq" => throw new QuitException
      case s => s
    }
  }

  def boneString(bone: Bone): String =
    s"${bone.left.toString}:${bone.right.toString}"

  def bonesToString(bones: Seq[Bone]): String = {
      bones.map(boneString).mkString(" ")
  }

  def printNormal(s: String) = println(s"\n$s")

  def printBold(s: String) = println(s"\n\n$s\n")

  def printError(s: String) = printNormal(s"  -- $s -- ")

  def printInfo(s: String) = printNormal(s)

  def printRequest(s: String) = print(s"\n$s ")

  def printAnnounce(s: String) = printBold(s)

  def printPlay(s: String) = printInfo(s"  <<<<<< $s >>>>>>")

  def printNewTurn(s: String) = printAnnounce(s"###### $s ######")

}

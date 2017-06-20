package dominoes

import dominoes._

object DominoesUtil5 { 

  def askUser(prompt: String): String = {
    //val otherOptions = "\n[or '..' for new game, or 'qq' to quit] "
    //val otherOptions = "['h' for help] "
    //val prompt = s"\n$question $otherOptions"
    print(s"\n$prompt ")
    val entered = scala.io.StdIn.readLine().trim

    entered match {
      case "h" => { 
        val helpString = s"\nAt any prompt, you can also type '..' to start a new game, or 'qq' to quit."
        println(helpString)
        askUser(prompt)
      }
      case ".." => throw new NewGameException
      case "qq" => throw new QuitException
      case s => s
    }
  }
 
}

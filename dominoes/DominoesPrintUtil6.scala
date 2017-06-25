package dominoes

import dominoes._

object DominoesPrintUtil6 { 

  /** Prompt user for info in a standard way, format the input, and offer
    * alternative user actions ie help, new game or quit.
    * @param prompt Prompt to put to user
    * @return formatted response from user 
    * @throws NewGameException if user requests new game 
    * @throws QuitException if user chooses to quit 
    */
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


  /** Convert bone to string for printing
    * @param bone Bone to convert
    * @return standardised bone string
    */
  def boneString(bone: Bone): String =
    s"${bone.left.toString}:${bone.right.toString}"


  /** Convert bone sequence to string for printing
    * @param bones Sequence to convert
    * @return String representing the sequence
    */
  def bonesToString(bones: Seq[Bone]): String = {
      bones.map(boneString).mkString(" ")
  }


  /** Printing for more common circumstances
    * @param s String to print
    */
  private def printNormal(s: String) = println(s"\n$s")


  /** Printing for less common circumstances 
    * @param s String to print
    */
  private def printBold(s: String) = println(s"\n\n$s\n")


  /** Printing for user error circumstances 
    * @param s String to print
    */
  def printError(s: String) = printNormal(s"  -- $s -- ")


  /** Printing for providing (non-error) information to user 
    * @param s String to print
    */
  def printInfo(s: String) = printNormal(s)


  /** Printing for requesting information from user 
    * @param s String to print
    */
  def printRequest(s: String) = print(s"\n$s ")


  /** Printing for making infrequent announcements  
    * @param s String to print
    */
  def printAnnounce(s: String) = printBold(s)


  /** Printing for announcing that a player has made a move  
    * @param s String to print
    */
  def printPlay(s: String) = printInfo(s"  <<<<<< $s >>>>>>")


  /** Printing for announcing a new turn has started 
    * @param s String to print
    */
  def printNewTurn(s: String) = printAnnounce(s"###### $s ######")

}

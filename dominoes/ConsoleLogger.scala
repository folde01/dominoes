package dominoes

/** A class providing a log method for showing debugging messages. 
  * @constructor Create a new logger for an object to use to show debug info.
  * @param id Some string identifying the object to be debugged, e.g. 'PLAYER1' or 'UI' 
  * @param whether it should print log messages - defaults to false
  * @param a counter to increment with and put in each log message
  */
class ConsoleLogger(
  id: String, 
  debug: Boolean = true, 
  private var counter: Int = 0) extends Logger { 

  /** Print a log message if in debug mode
    * @param string to print
    */
  def log(s: String): Unit = {
    if (debug) {
      println(id + ":" + counter + ": " + s)
      counter += 1
    }
  }

}

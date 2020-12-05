package Chapter1007

trait ConsoleLogger extends Logger {
  var newField2 = 3.0
  def log(msg: String) { println(msg) }
}

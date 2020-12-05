package Chapter1007

object Main extends App {
  val acct = new SavingsAccount with ConsoleLogger
  acct.withdraw(100)
  println("NEW FIELD1 " + acct.newField.toString)
  println("NEW FIELD2 " + acct.newField2.toString)
}


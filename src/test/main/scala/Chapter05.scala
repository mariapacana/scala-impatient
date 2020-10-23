import scala.beans.BeanProperty

object Chapter05 {

  /**
   * Task 1:
   *  Improve the Counter class in Section 5.1, "Simple Classes and Parameterless Methods"
   *  on page 51 so that it doesn't turn negative at Int.MaxValue
   */
  class Counter01(private var value: Int = 0) {

    def increment(): Int = {
      if (value == Int.MaxValue) {
        throw new IllegalStateException("counter reached Int.MaxValue")
      }

      value += 1
      value
    }

    def current: Int = value
  }

  /**
   * Task 2:
   *  Write a class BankAccount with methods deposit and withdraw,
   *  and a read-only property balance.
   */
  class BankAccount02 {

    private var amount: Int = 0

    def deposit(sum: Int): Unit = {
      amount += sum
    }

    def withdraw(sum: Int): Unit = {
      amount -= sum
    }

    def balance: Int = amount
  }

  /**
   * Task 3:
   *  Write a class Time with read-only properties hours and minutes and a method
   *
   *    before(other: Time): Boolean
   *
   *  that checks whether this time comes before the other.
   *
   *  A Time object should be constructed as new Time(hrs, min), where hrs is in
   *  military time format (between 0 and 23).
   */
  class Time03(private val hours: Int, private val minutes: Int) {

    def before(other: Time03): Boolean = {
      if (hours < other.hours) true
      else if (hours == other.hours && minutes < other.minutes) true
      else false
    }
  }

  /**
   * Task 4:
   *  Reimplement the Time class from the preceding exercise so that the internal
   *  representation is the number of minutes since midnight (between 0 and 24 × 60 – 1).
   *  Do not change the public interface. That is, client code should be unaffected by your change.
   */
  class Time04(hrs: Int, min: Int) {

    private val minutes: Int = (hrs * 60) + min

    def before(other: Time04): Boolean = {
      minutes < other.minutes
    }
  }

  /**
   * Task 5:
   *  Make a class Student with read-write JavaBeans properties name (of type String )
   *  and id (of type Long ). What methods are generated? (Use javap to check.)
   *  Can you call the JavaBeans getters and setters in Scala? Should you?
   */
  class Student05 {

    @BeanProperty
    var name: String = ""

    @BeanProperty
    var id: Long = 0
  }

  /**
   * Task 6:
   *  In the Person class of Section 5.1, "Simple Classes and Parameterless Methods,"
   *  on page 51, provide a primary constructor that turns negative ages to 0.
   */
  class Person06(inAge: Int) {

    var age: Int = if (inAge < 0) 0 else inAge
  }

  /**
   * Task 7:
   *  Write a class Person with a primary constructor that accepts a string containing
   *  a first name, a space, and a last name, such as new Person("Fred Smith").
   *  Supply read-only properties firstName and lastName.
   *  Should the primary constructor parameter be a var, a val, or a plain parameter? Why?
   */
  class Person07 private(names: Array[String]) {

    val firstName: String = names(0)
    val lastName: String = names(1)

    def this(name: String) {
      this(Person07.parseName(name))
    }
  }

  private object Person07 {
    def parseName(name: String): Array[String] = {
      val names = name.split(" ")
      if (names.length < 2) {
        throw new IllegalArgumentException("name should contain a first name, a space" +
          ", and a last name, such as \"Fred Smith\"")
      }

      names
    }
  }

  /**
   * Task 8:
   *  Make a class Car with read-only properties for manufacturer, model name,
   *  and model year, and a read-write property for the license plate.
   *  Supply four constructors. All require the manufacturer and model name.
   *  Optionally, model year and license plate can also be specified in the constructor.
   *  If not, the model year is set to -1 and the license plate to the empty string.
   *  Which constructor are you choosing as the primary constructor? Why?
   */
  class Car08(val manufacturer: String,
              val modelName: String,
              val modelYear: Int = -1,
              val licensePlate: String = "") {

    def this(manufacturer: String, modelName: String, licensePlate: String) {
      this(manufacturer, modelName, -1, licensePlate)
    }

// Can be removed
//   see http://stackoverflow.com/questions/24480989/scala-auxilliary-constructor-behavior
//
//    def this(manufacturer: String, modelName: String, modelYear: Int) {
//      this(manufacturer, modelName, modelYear, "")
//    }
//
//    def this(manufacturer: String, modelName: String) {
//      this(manufacturer, modelName, -1)
//    }
  }

  /**
   * Task 9:
   *  Reimplement the class of the preceding exercise in Java, C#, or C++ (your choice).
   *  How much shorter is the Scala class?
   */
  // see src/main/java/Chapter05Car.java

  /**
   * Task 10:
   *  Consider the class
   *
   *    class Employee(val name: String, var salary: Double) {
   *      def this() { this("John Q. Public", 0.0) }
   *    }
   *
   *  Rewrite it to use explicit fields and a default primary constructor.
   *  Which form do you prefer? Why?
   */
  class Employee10 {
    val name: String = "John Q. Public"
    var salary: Double = 0.0
  }
}

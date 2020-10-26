import scala.beans.BeanProperty

object Chapter05 {

  /**
   * Task 1:
   *  Improve the Counter class in Section 5.1, "Simple Classes and Parameterless Methods"
   *  on page 51 so that it doesn't turn negative at Int.MaxValue
   */

  class Counter01(private var value: Int) {
    def increment(): Int = {
      if (value == Int.MaxValue) {
        value = 0
      } else {
        value += 1
      }
      value
    }
    def current = value
    def isLess(other : Counter01) = value < other.value
  }

  /*
   * Task 2:
   *  Write a class BankAccount with methods deposit and withdraw,
   *  and a read-only property balance.
   */
  class BankAccount02 {
    private var balance: Int = 0

    def currentBalance: Int = balance

    def deposit(amount: Int): Int = {
      balance += amount
      balance
    }

    def withdraw(amount: Int): Int = {
      balance -= amount
      balance
    }

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
  class Time03 {

    private var _hours = 0
    private var _minutes = 0

    // Set hrs and mins to a sentinel value if they are outside the range.
    def this(hrs: Int, min: Int) {
      this()
      this._hours = if (hrs <= 23 && hrs >=0) hrs else -1
      this._minutes = if (min <= 60 && min >=0) min else -1
    }

    def hours = this._hours
    def minutes = this._minutes

    def before(other: Time03): Boolean = {
      this._hours < other._hours || (this._hours == other._hours && this._minutes < other._minutes)
    }
  }

  /**
   * Task 4:
   *  Reimplement the Time class from the preceding exercise so that the internal
   *  representation is the number of minutes since midnight (between 0 and 24 × 60 – 1).
   *  Do not change the public interface. That is, client code should be unaffected by your change.
   */
  class Time04 {
    private var _hours = 0
    private var _minutes = 0
    private var minSinceMidnight = 0
    private val minPerHour = 60

    // Set hrs and mins to a sentinel value if they are outside the range.
    def this(hrs: Int, min: Int) {
      this()

      this._hours = if (hrs <= 23 && hrs >=0) hrs else -1
      this._minutes = if (min <= 60 && min >=0) min else -1
      if (this._hours != -1 && this._minutes != -1) {
        this.minSinceMidnight = this._hours*this.minPerHour+this._minutes
      } else {
        this.minSinceMidnight = -1
      }
    }

    def before(other: Time04): Boolean = {
      this.minSinceMidnight < other.minSinceMidnight
    }
  }


  /**
   * Task 5:
   *  Make a class Student with read-write JavaBeans properties name (of type String )
   *  and id (of type Long ). What methods are generated? (Use javap to check.)
   *  Can you call the JavaBeans getters and setters in Scala? Should you?
   */

  /**
   * Task 6:
   *  In the Person class of Section 5.1, "Simple Classes and Parameterless Methods,"
   *  on page 51, provide a primary constructor that turns negative ages to 0.
   */
  class Person06(var age: Int) {
    if (age < 0) age = 0
  }

  /**
   * Task 7:
   *  Write a class Person with a primary constructor that accepts a string containing
   *  a first name, a space, and a last name, such as new Person("Fred Smith").
   *  Supply read-only properties firstName and lastName.
   *  Should the primary constructor parameter be a var, a val, or a plain parameter? Why?
   *
   *  I'd go with a plain parameter, since a plain parameter is object-private and no other
   *  object of this class needs to know about it.
   */
  class Person07(name: String) {
    private[this] val nameList = name.split(" ")
    val firstName: String = nameList(0)
    val lastName: String = nameList(1)
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
class Car08(val manufacturer: String, val modelName: String) {
    private var _modelYear: Int = -1
    var licensePlate: String = ""

    def modelYear = _modelYear

    def this(_manufacturer: String, _modelName: String, __modelYear: Int) {
      this(_manufacturer, _modelName)
      this._modelYear = __modelYear
    }

    def this(_manufacturer: String, _modelName: String, _licensePlate: String) {
      this(_manufacturer, _modelName)
      this.licensePlate = _licensePlate
    }

    def this(_manufacturer: String, _modelName: String, __modelYear: Int, _licensePlate: String) {
      this(_manufacturer, _modelName, __modelYear)
      this.licensePlate = _licensePlate
    }
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

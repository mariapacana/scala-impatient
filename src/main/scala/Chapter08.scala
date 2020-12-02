import scala.collection.mutable.ListBuffer

/**
 * Task 1:
 *
 * Extend the following `BankAccount` class to a `CheckingAccount` class that charges $1
 * for every deposit and withdrawal.
 * {{{
 * class BankAccount(initialBalance: Double) {
 *   private var balance = initialBalance
 *   def deposit(amount: Double) = { balance += amount; balance }
 *   def withdraw(amount: Double) = { balance -= amount; balance }
 * }
 * }}}
 */
class BankAccount(initialBalance: Double) {
  protected var balance: Double = initialBalance

  def deposit(amount: Double): Double = {
    balance += amount; balance
  }

  def withdraw(amount: Double): Double = {
    balance -= amount; balance
  }
}

class CheckingAccount(initialBalance: Double) extends BankAccount(initialBalance) {
  val transactionCharge: Double = 1.0

  override def deposit(amount: Double): Double = {
    super.deposit(amount - transactionCharge)
  }

  override def withdraw(amount: Double): Double = {
    super.withdraw(amount + transactionCharge)
  }
}

/**
 * Task 2:
 *
 * Extend the `BankAccount` class of the preceding exercise into a class `SavingsAccount`
 * that earns interest every month (when a method `earnMonthlyInterest` is called)
 * and has three free deposits or withdrawals every month. Reset the transaction
 * count in the `earnMonthlyInterest` method.
 */
class SavingsAccount(initialBalance: Double) extends BankAccount(initialBalance) {

  private var freeTransactions = 3
  val transactionCharge: Double = 1.0
  val monthlyInterest: Double = 0.01

  def earnMonthlyInterest(): Double = {
    freeTransactions = 3
    balance = balance*(1 + monthlyInterest)
    balance
  }

  def getBalance: Double = balance

  override def deposit(amount: Double): Double = {
    val _transactionCharge = if (freeTransactions > 0) 0 else transactionCharge
    freeTransactions -= 1
    super.deposit(amount - _transactionCharge)
  }

  override def withdraw(amount: Double): Double = {
    val _transactionCharge = if (freeTransactions > 0) 0 else transactionCharge
    freeTransactions -= 1
    super.withdraw(amount + _transactionCharge)
  }
}

/**
 * Task 3:
 *
 * Consult your favorite Java or C++ textbook that is sure to have an example
 * of a toy inheritance hierarchy, perhaps involving employees, pets, graphical
 * shapes, or the like. Implement the example in Scala.
 */
class Vehicle(protected val brand: String) {
  def honk(): Unit = {
    println("Toot!")
  }
}

class Car(val modelName: String, brand: String) extends Vehicle(brand) {
  def getBrand: String = brand
}

object Car {
  def apply(modelName: String, brand: String): Car = new Car(modelName, brand)
}

/**
 * Task 4:
 *
 * Define an abstract class `Item` with methods `price` and `description`. A `SimpleItem`
 * is an item whose `price` and `description` are specified in the constructor. Take advantage
 * of the fact that a val can override a def. A `Bundle` is an item that contains other items.
 * Its price is the sum of the prices in the bundle. Also provide a mechanism for adding items
 * to the bundle and a suitable description method.
 */

abstract class Item {
  def price: Double

  def description: String
}

class SimpleItem(val price: Double, val description: String) extends Item

class Bundle extends Item {

  var items: ListBuffer[Item] = ListBuffer()

  def price: Double = items.map(_.price).sum

  def description: String = items.map(_.description).mkString(", ")

  def addItem(item: Item): Bundle = {
    items += item
    this
  }
}

/**
 *
 * Task 5:
 *
 * Design a class `Point` whose x and y coordinate values can be provided in a constructor.
 * Provide a subclass `LabeledPoint` whose constructor takes a `label` value and `x` and `y`
 * coordinates, such as
 * {{{
 *   new LabeledPoint("Black Thursday", 1929, 230.07)
 * }}}
 */
package task0805 {
  class Point(val x: Double, val y: Double)
  
  object Point {
    def apply(x: Double, y: Double): Point = new Point(x, y)
  }

  class LabeledPoint(val label: String, x: Double, y: Double) extends Point(x, y)
}

/**
 * Task 6:
 *
 * Define an abstract class `Shape` with an abstract method `centerPoint` and subclasses
 * `Rectangle` and `Circle`. Provide appropriate constructors for the subclasses and
 * override the `centerPoint` method in each subclass.
 */
package task0806 {
  import task0805.{Point => MyPoint}

  abstract class Shape {
    def centerPoint: MyPoint
  }

  class Circle(val radius: Double, val centerPoint: MyPoint) extends Shape

  class Rectangle(val width: Double, val height: Double, val centerPoint: MyPoint) extends Shape {
    val topLeft = MyPoint(centerPoint.x - width/2, centerPoint.y + height/2)
    val bottomLeft = MyPoint(centerPoint.x - width/2, centerPoint.y - height/2)
    val topRight = MyPoint(centerPoint.x + width/2, centerPoint.y + height/2)
    val bottomRight = MyPoint(centerPoint.x + width/2, centerPoint.y - height/2)
  }
}


/**
 * Task 7:
 *
 * Provide a class `Square` that extends `java.awt.Rectangle` and has three constructors:
 * one that constructs a square with a given corner point and width,
 * one that constructs a square with corner (0, 0) and a given width,
 * and one that constructs a square with corner (0, 0) and width 0.
 */
class Square(val cornerX: Int, val cornerY: Int, val squareWidth: Int)
  extends java.awt.Rectangle(cornerX, cornerY, squareWidth, squareWidth) {

  def this(width: Int) {
    this(0, 0, width)
  }

  def this() {
    this(0)
  }
}

/**
 * Task 8:
 *
 * Compile the `Person` and `SecretAgent` classes in Section 8.6, “Overriding Fields,” on page 91
 * and analyze the class files with `javap`.
 * How many name fields are there?
 * How many name getter methods are there?
 * What do they get?
 *
 *
 * There are two name fields, one in Person and one in SecretAgent.
 * These fields are 'private final' in both cases.
 * Final means that the two name fields can only be assigned once.
 * Because of Java name hiding, SecretAgent will only use its own name field, and not Person's name field.
 * There are two name() getter methods, one in each class.
 * SecretAgent's name() getter method will get its name field, which has been permanently set to 'secret'.
 */
// Person
// javap -private /Users/maria.pacana/Documents/PDP/scala-impatient/target/scala-2.12/classes/Person.class
// - name() getter method and name private variable
// - toString() method - parens because it's being executed
class Person(val name: String) {
  override def toString = s"${getClass.getName}[name=$name]"
}

// SecretAgent
//javap -private /Users/maria.pacana/Documents/PDP/scala-impatient/target/scala-2.12/classes/SecretAgent.class
// - no codename object-private field, because it's not used in any methods
// - name getter overrides Person getter
// - private toString field
// - toString getter overrides Person getter
class SecretAgent(codename: String) extends Person(codename) {
  override val name = "secret"
  override val toString = "secret"
}

/**
 * Task 9:
 *
 * In the `Creature` class of Section 8.10, "Construction Order and Early Definitions,"
 * on page 94, replace `val range` with a `def`. What happens when you also use a
 * `def` in the `Ant` subclass? What happens when you use a `val` in the subclass?
 * Why?
 *
 * When both Creature and Ant use def, env's length is 2 and the range is 2 because
 * Ant overrides the `range` method.
 *
 * When Ant uses val and Creature uses Def, env's length is 0. Ant overrides range
 * with a val, but the underlying field hasn't been initialized.
 * - Creature's (the superclass's) constructor runs first
 * - Creature initializes the env array using range from Ant (not initialized)
 * - Ant's constructor runs and sets range = 2
 *
 */
class Creature1 {
  def range: Int = 10
  val env: Array[Int] = new Array[Int](range)
}

class Ant1 extends Creature1 {
  override def range = 2
}

class Creature2 {
  def range: Int = 10
  val env: Array[Int] = new Array[Int](range)
}

class Ant2 extends Creature2 {
  override val range = 2
}

/**
 * Task 10:
 *
 * The file `scala/collection/immutable/Stack.scala` contains the definition
 * {{{
 * class Stack[A] protected (protected val elems: List[A])
 * }}}
 * Explain the meanings of the `protected` keywords. (Hint: Review the discussion
 * of private constructors in Chapter 5.)
 *
 * The 'elems' property is protected, meaning that only members
 * of the Stack class or subclasses can access it.
 *
 * The constructor itself is protected. Only an auxiliary constructor or a subclass
 * primary constructor can call this protected primary constructor, and you can't
 * just use the 'new' keyword when creating a Stack.
 */

/**
 * Task 11:
 *
 * Define a value class Point that packs integer x and y coordinates into a Long (which you
 * should make private).
 */
class Point(private val coordinates: Long) extends AnyVal {
  def x: Int = ???
  def y: Int = ???
  override def toString = ???
}

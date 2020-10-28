/**
 * Task 1:
 *  Write an object Conversions with methods inchesToCentimeters, gallonsToLiters,
 *  and milesToKilometers.
 */

object Conversions {
  private val inchToCentimeter = 2.54
  private val gallonToLiter = 3.79
  private val mileToKilometer = 1.61

  def inchesToCentimeters(inches: Double): Double = {
    inches*inchToCentimeter
  }

  def gallonsToLiters(gallons: Double): Double = {
    gallons*gallonToLiter
  }

  def milesToKilometers(miles: Double): Double = {
    miles*mileToKilometer
  }
}

/**
 * Task 2:
 *  The preceding problem wasn't very object-oriented. Provide a general super-class
 *  UnitConversion and define objects InchesToCentimeters, GallonsToLiters, and
 *  MilesToKilometers that extend it.
 */
abstract class UnitConversion {
  val conversionRatio: Double

  def convert(original: Double): Double = {
    original*conversionRatio
  }
}

object InchesToCentimeters extends UnitConversion {
  val conversionRatio = 2.54
}

object GallonsToLiters extends UnitConversion {
  val conversionRatio = 3.79
}

object MilesToKilometers extends UnitConversion {
  val conversionRatio = 1.61
}

/**
 * Task 3:
 *  Define an Origin object that extends java.awt.Point. Why is this not actually a good idea?
 *  (Have a close look at the methods of the Point class.)
 */

/**
 * Task 4:
 *
 * Define a Point class with a companion object so that you can construct Point
 * instances as Point(3, 4), without using new.
 */
class Point(val x: Int, val y: Int)

object Point {
  def apply(x: Int, y: Int): Point = new Point(x, y)
}

/**
 * Task 5:
 *  Write a Scala application, using the App trait, that prints the command-line
 *  arguments in reverse order, separated by spaces. For example:
 *  <blockquote><code>
 *    scala Reverse Hello World
 *  </code></blockquote>
 *  should print
 *  <blockquote><code>
 *    World Hello
 *  </code></blockquote>
 */
object Reverse extends App {
 println(args.reverse.mkString(" "))
}

/**
 * Task 6:
 *  Write an enumeration describing the four playing card suits so that the toString method
 *  returns ♣, ♦, ♥, or ♠.
 */
object PlayingCard extends Enumeration {
  val Clubs = Value("♣")
  val Diamonds = Value("♦")
  val Hearts = Value("♥")
  val Spades = Value("♠")
}

/**
 * Task 7:
 *   Implement a function that checks whether a card suit value from the preceding exercise
 *   is red.
 */
object CardColors {
  def check(suit: PlayingCard.Value): Boolean = {
    if (suit.equals(PlayingCard.Diamonds) || suit.equals(PlayingCard.Hearts)) true else false
  }
}

/**
 * Task 8:
 *   Write an enumeration describing the eight corners of the RGB color cube.
 *   As IDs, use the color values (for example, 0xff0000 for Red).
 */
object RGB extends Enumeration {
  val Black = Value(0x000000, "Black")
  val White = Value(0xffffff, "White")
  val Red = Value(0xff0000, "Red")
  val Lime = Value(0x00ff00, "Lime")
  val Blue = Value(0x0000ff, "Blue")
  val Yellow = Value(0xffff00, "Yellow")
  val Cyan = Value(0x00ffff, "Cyan")
  val Magenta = Value(0xff00ff, "Magenta")
}

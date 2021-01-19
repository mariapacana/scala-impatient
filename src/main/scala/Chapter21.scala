import java.awt.geom.Point2D
import java.awt.{Point => jPoint}

import scala.annotation.tailrec
import scala.collection.immutable.IndexedSeq
import scala.io.StdIn
import scala.math.abs
import scala.language.implicitConversions

object Chapter21 {

  /**
   * Task 1:
   *
   * How does `->` work? That is, how can `"Hello" -> 42` and `42 -> "Hello"` be pairs
   * `("Hello", 42)` and `(42, "Hello")`? Hint: `Predef.ArrowAssoc`.
   */

  /**
   * Task 2:
   *
   * Define an operator `+%` that adds a given percentage to a value. For example,
   * `120 +% 10` should be `132`. Use an implicit class.
   */
  implicit class RichInt(from: Int) {
    implicit def +%(percentage: Double): Double = (from*(100 + percentage)/100).toInt
  }

  /**
   * Task 3:
   *
   * Define a `!` operator that computes the factorial of an integer. For example,
   * `5!` is `120`. Use an implicit class.
   */
  implicit class MyRichInt(val from: Int) {
    def ! : Int = go(from)

    def go(i: Int): Int = {
      if (i == 1) 1 else i*go(i-1)
    }
  }

  /**
   * Task 4:
   *
   * Some people are fond of "fluent APIs" that read vaguely like English sentences.
   * Create such an API for reading integers, floating-point numbers, and strings from the console.
   * For example:
   * {{{
   * Read in aString askingFor "Your name" and
   *   anInt askingFor "Your age" and
   *   aDouble askingFor "Your weight".
   * }}}
   */

  /**
   * Task 5:
   *
   * Provide the machinery that is needed to compute
   * {{{
   * smaller(Fraction(1, 7), Fraction(2, 9))
   * }}}
   * with the `Fraction` class of Chapter 11.
   * Supply a `class RichFraction` that extends `Ordered[Fraction]`.
   */
  class Fraction(n: Int, d: Int) {
    val num: Int = if (d == 0) 1 else n * sign(d) / gcd(n, d);
    val den: Int = if (d == 0) 0 else d * sign(d) / gcd(n, d);
    override def toString = num + "/" + den
    override def equals(that: Any): Boolean = that match {
      case that: Fraction => num == that.num && den == that.den
      case _ => false
    }
    def sign(a: Int): Int = if (a > 0) 1 else if (a < 0) -1 else 0
    def gcd(a: Int, b: Int): Int = if (b == 0) abs(a) else gcd(b, a % b)
    def *(other: Fraction) = new Fraction(num * other.num, den * other.den)
  }

  object Fraction {
    def apply(n: Int, d: Int): Fraction = new Fraction(n, d)
  }

  implicit class RichFraction(from: Fraction) extends Ordered[Fraction] {
    // Cheating somewhat by casting to Double
    def compare(that: Fraction): Int = this.toDouble.compareTo(that.toDouble)

    def toDouble: Double = from.num.toDouble / from.den
  }

  // Given on p 329
  def smaller[T](a: T, b: T)(implicit order: T => Ordered[T]): T =
    if (order(a) < b) a
    else b

  /**
   * Task 6:
   *
   * Compare objects of the `class java.awt.Point` by lexicographic comparison.
   */
  implicit object LexicographicPointOrdering extends Ordering[jPoint] {
    def compare(x: jPoint, y: jPoint): Int = x.toString.compareTo(y.toString)
  }

  /**
   * Task 7:
   *
   * Continue the previous exercise, comparing two points according to their distance to
   * the origin. How can you switch between the two orderings?
   *
   * Solution:
   * You can switch between the two orderings by importing one or the other.
   */
  implicit object DistancePointOrdering extends Ordering[jPoint] {
    def compare(x: jPoint, y: jPoint): Int = distance(x).compareTo(distance(y))

    def distance(x: jPoint): Double = Point2D.distance(x.getX, x.getY, 0, 0)
  }

  /**
   * Task 8:
   *
   * Use the `implicitly` command in the REPL to summon the implicit objects described in
   * Section 21.5, "Implicit Parameters," on page 328 and
   * Section 21.6, "Implicit Conversions with Implicit Parameters," on page 329.
   * What objects do you get?
   */
  case class Delimiters(left: String, right: String)
  def quote(what: String)(implicit delims: Delimiters) =
    delims.left + what + delims.right
  quote("Bonjour le monde")(Delimiters("«", "»"))
  object FrenchPunctuation {
    implicit val quoteDelimiters = Delimiters("«", "»")
  }
  import FrenchPunctuation._
  quote("Bonjour le monde")
  /**
   * From Section 2.6:
   *
   * scala> implicitly[Delimiters]
   * res4: Delimiters = Delimiters(«,»)
   */
//  def smaller[T](a: T, b: T)(implicit order: T => Ordered[T]) = if (order(a) < b) a else b
//  smaller(40, 2)
//  smaller("Hello", "World")
//  def smaller[T](a: T, b: T)(implicit order: T => Ordered[T]) =
//    if (a < b) a else b // Can omit call to order
//  smaller(40, 2)
//  smaller("Hello", "World")
  /**
   * Trying to call `implicitly[order]` here doesn't work.
   */

  /**
   * Task 9:
   *
   * Explain why `Ordering` is a type class and why `Ordered` is not.
   */

  /**
   * Task 10:
   *
   * Generalize the `average` method in Section 21.8, "Type Classes," on page 331 to a `Seq[T]`.
   */
  def average[T](ts: T*)(implicit ev: NumberLike[T]): T = {
    val sum = ts.reduce(ev.plus)
    ev.divideBy(sum, ts.length)
  }

  /**
   * Task 11:
   *
   * Make `String` a member of the `NumberLike` type class in Section 21.8, "Type Classes,"
   * on page 331. The `divBy` method should retain every `n`th letter, so that `average("Hello", "World")`
   * becomes "Hlool".
   */
  trait NumberLike[T] {
    def plus(x: T, y: T): T
    def divideBy(x: T, n: Int): T
  }

  object NumberLike {
    implicit object NumberLikeString extends NumberLike[String] {
      def plus(x: String, y: String): String = x + y
      def divideBy(x: String, n: Int): String = x.zipWithIndex.collect { case (c, i) if i % n == 0 => c }.mkString("")
    }

    implicit object NumberLikeDouble extends NumberLike[Double] {
      def plus(x: Double, y: Double): Double = x + y
      def divideBy(x: Double, n: Int): Double = x / n
    }

    implicit object NumberLikeBigDecimal extends NumberLike[BigDecimal] {
      def plus(x: BigDecimal, y: BigDecimal): BigDecimal = x + y
      def divideBy(x: BigDecimal, n: Int): BigDecimal = x / n
    }
  }

  /**
   * Task 12:
   *
   * Look up the `=:=` object in `Predef.scala`. Explain how it works.
   *
   */

  /**
   * Task 13:
   *
   * The result of `"abc".map(_.toUpper)` is a `String`, but the result of `"abc".map(_.toInt)`
   * is a Vector. Find out why.
   */

}

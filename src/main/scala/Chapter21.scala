import java.awt.Point
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

  /**
   * Task 7:
   *
   * Continue the previous exercise, comparing two points according to their distance to
   * the origin. How can you switch between the two orderings?
   *
   */

  /**
   * Task 8:
   *
   * Use the `implicitly` command in the REPL to summon the implicit objects described in
   * Section 21.5, "Implicit Parameters," on page 328 and
   * Section 21.6, "Implicit Conversions with Implicit Parameters," on page 329.
   * What objects do you get?
   *
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

  /**
   * Task 11:
   *
   * Make `String` a member of the `NumberLike` type class in Section 21.8, "Type Classes,"
   * on page 331. The `divBy` method should retain every `n`th letter, so that `average("Hello", World")`
   * becomes "Hlool".
   */

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

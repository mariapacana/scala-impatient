import Chapter11.Fraction
import java.awt.Point
import scala.annotation.tailrec
import scala.collection.immutable.IndexedSeq
import scala.io.StdIn
import scala.language.implicitConversions


object Chapter21 {

  /**
   * Task 1:
   *
   * How does `->` work? That is, how can `"Hello" -> 42` and `42 -> "Hello"` be pairs
   * `("Hello", 42)` and `(42, "Hello")`? Hint: `Predef.any2ArrowAssoc`.
   */

  /**
   * Task 2:
   *
   * Define an operator `+%` that adds a given percentage to a value. For example,
   * `120 +% 10` should be `132`. Hint: Since operators are methods, not functions,
   * you will also need to provide an `implicit`.
   */

  /**
   * Task 3:
   *
   * Define a `!` operator that computes the factorial of an integer. For example,
   * `5!` is `120`. You will need an enrichment class and an implicit conversion.
   */

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
   * in Section 21.6, "Implicit Conversions with Implicit Parameters," on page 310.
   * Supply a `class RichFraction` that extends `Ordered[Fraction]`.
   */

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
   * Solution:
   *
   * We can switch between the two orderings by importing appropriate `implicit class`:
   * {{{
   * import Chapter21.LexicographicPointOrdering
   * }}}
   * or
   * {{{
   * import Chapter21.DistancePointOrdering
   * }}}
   */

  /**
   * Task 8:
   *
   * Use the `implicitly` command in the REPL to summon the implicit objects described in
   * Section 21.5, "Implicit Parameters," on page 309 and
   * Section 21.6, "Implicit Conversions with Implicit Parameters," on page 310.
   * What objects do you get?
   *
   */

  /**
   * Task 9:
   *
   * Look up the `=:=` object in `Predef.scala`. Explain how it works.
   *
   */

  /**
   * Task 10:
   *
   * The result of `"abc".map(_.toUpper)` is a `String`, but the result of `"abc".map(_.toInt)`
   * is a Vector. Find out why.
   */

}

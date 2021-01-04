import scala.reflect.ClassTag

object Chapter18 {
  /**
   * Task 1:
   *
   * Define an immutable `class Pair[T, S]` with a method `swap` that returns a new pair with
   * the components swapped.
   */
  object task1801 {
    class Pair[T,S](val first: T, val second: S) {
      def apply(first: T, second: S) = new Pair(first, second)

      def swap() = new Pair(second, first)
    }
  }

  /**
   * Task 2:
   *
   * Define a mutable `class Pair[T]` with a method `swap` that swaps the components of the pair.
   */
  object task1802 {
    class Pair[T](var first: T, var second: T) {
      def apply(first: T, second: T): Pair[T] = new Pair(first, second)

      def swap(): Unit = {
        val tmp = first
        first = second
        second = tmp
      }
    }
  }

  /**
   * Task 3:
   *
   * Given a `class Pair[T, S]`, write a generic method `swap` that takes a pair as its argument
   * and returns a new pair with the components swapped.
   */
  object task1803 {
    def swap[T, S](pair: task1801.Pair[T, S]) = new task1801.Pair(pair.second, pair.first)
  }

  /**
   * Task 4:
   *
   * Why don't we need a lower bound for the `replaceFirst` method in Section 17.3,
   * "Bounds for Type Variables”, on page 232 if we want to replace the first component of
   * a `Pair[Person]` with a `Student`?
   *
   * Solution:
   * Because a `Student` is a subtype of a `Person`, so the compiler still correctly infers that
   * the type is `Pair[Person]`.
   */
  object task1804 {
    class Person(val name: String)
    class Student(override val name: String) extends Person(name)

    class Pair[T](val first: T, val second: T) {
      def replaceFirst(newFirst: T) = new Pair(newFirst, second)
    }
  }

  /**
   * Task 5:
   *
   * Why does `RichInt` implement `Comparable[Int]` and not `Comparable[RichInt]`?
   *
   * Solution:
   * View bounds...
   */

  /**
   * Task 6:
   *
   * Write a generic method `middle` that returns the middle element from any `Iterable[T]`.
   * For example, `middle("World")` is 'r'.
   */
  object task1806 {
    def middle[T : ClassTag](it: Iterable[T]): Option[T] = {
      if (it.isEmpty) return None
      val mid = (it.size / 2)
      Some(it.slice(0, mid + 1).last)
    }
  }

  /**
   * Task 7:
   *
   * Look through the methods of the `Iterable[+A]` trait. Which methods use the type parameter `A`?
   * Why is it in a covariant position in these methods?
   *
   */

  /**
   * Task 8:
   *
   * In Section 17.10, "Co- and Contravariant Positions", on page 238, the `replaceFirst` method
   * has a type bound. Why can't you define an equivalent method on a mutable `Pair[T]`?
   * {{{
   *   def replaceFirst[R >: T](newFirst: R) { first = newFirst } // Error
   * }}}
   */

  /**
   * Task 9:
   *
   * It may seem strange to restrict method parameters in an immutable `class Pair[+T]`. However,
   * suppose you could define
   * {{{
   *   def replaceFirst(newFirst: T)
   * }}}
   * in a `Pair[+T]`. The problem is that this method can be overridden in an unsound way.
   * Construct an example of the problem. Define a subclass `NastyDoublePair` of `Pair[Double]`
   * that overrides `replaceFirst` so that it makes a pair with the square root of `newFirst`.
   * Then construct the call `replaceFirst("Hello")` on a `Pair[Any]` that is actually
   * a `NastyDoublePair`.
   */

  /**
   * Task 10:
   *
   * Given a mutable `Pair[S, T]` class, use a type constraint to define a `swap` method that can
   * be called if the type parameters are the same.
   */

}

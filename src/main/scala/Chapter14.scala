import scala.Double
import scala.reflect.ClassTag

object Chapter14 {

  /**
   * Task 1:
   *
   * Your Java Development Kit distribution has the source code for much of the JDK in the
   * `src.zip` file. Unzip and search for case labels (regular expression `case [^:]+:`).
   * Then look for comments starting with `//` and containing `[Ff]alls? thr` to catch comments
   * such as `// Falls through` or `// just fall thru`.
   * Assuming the JDK programmers follow the Java code convention, which requires such a comment,
   * what percentage of cases falls through?
   */

  /**
   * Task 2:
   *
   * Using pattern matching, write a function `swap` that receives a pair of integers and
   * returns the pair with the components swapped.
   */
  def swap(pair: (Int, Int)): (Int, Int) = {
    pair match {
      case (a, b) => (b, a)
    }
  }

  /**
   * Task 3:
   *
   * Using pattern matching, write a function `swap` that swaps the first two elements of
   * an array provided its length is at least two.
   */
  def swap2[T: ClassTag](arr: Array[T]): Array[T] = {
    arr match {
      case Array(a, b, rest @ _*) => Array(b, a) ++ rest
      case _ => arr
    }
  }

  /**
   * Task 4:
   *
   * Add a case class `Multiple` that is a subclass of the `Item` class. For example,
   * `Multiple(10, Article("Blackwell Toaster", 29.95))` describes ten toasters. Of course,
   * you should be able to handle any items, such as bundles or multiples, in the second argument.
   * Extend the `price` function to handle this new case.
   */
  abstract class Item
  case class Article(description: String, price: Double) extends Item
  case class Bundle(description: String, discount: Double, items: Item*) extends Item
  case class Multiple(number: Int, item: Item) extends Item

  def price(it: Item): Double = it match {
    case Article(_, p) => p
    case Bundle(_, disc, its @ _*) => its.map(price).sum - disc
    case Multiple(n, it) => price(it)*n
  }

  /**
   * Task 5:
   *
   * One can use lists to model trees that store values only in the leaves. For example, the list
   * `((3 8) 2 (5))` describes the tree
   * {{{
   *     *
   *    /|\
   *   * 2 *
   *  /\   |
   * 3 8   5
   * }}}
   * However, some of the list elements are numbers and others are lists. In Scala, you cannot
   * have heterogeneous lists, so you have to use a `List[Any]`. Write a `leafSum` function to
   * compute the sum of all elements in the leaves, using pattern matching to differentiate
   * between numbers and lists.
   */
  def leafSum(lst: List[Any]): Double =
    (lst map { i: Any => {
      i match {
        case d: Double => d
        case i: Int => i.toDouble
        case l: List[Any] => leafSum(l)
      }
    }}).sum

  /**
   * Task 6:
   *
   * A better way of modeling such trees is with case classes. Let's start with binary trees.
   * {{{
   * sealed abstract class BinaryTree
   * case class Leaf(value: Int) extends BinaryTree
   * case class Node(left: BinaryTree, right: BinaryTree) extends BinaryTree
   * }}}
   * Write a function to compute the sum of all elements in the leaves.
   */

  /**
   * Task 7:
   *
   * Extend the tree in the preceding exercise so that each node can have an arbitrary number
   * of children, and reimplement the `leafSum` function. The tree in exercise 5 should be as
   * {{{
   *  Node(Node(Leaf(3), Leaf(8)), Leaf(2), Node(Leaf(5)))
   * }}}
   */

  /**
   * Task 8:
   *
   * Extend the tree in the preceding exercise so that each non-leaf node stores an operator
   * in addition to the child nodes. Then write a function `eval` that computes the value.
   * For example, the tree
   * {{{
   *     +
   *    /|\
   *   * 2 -
   *  /\   |
   * 3 8   5
   * }}}
   * has value `(3 x 8) + 2 + (-5) = 21`.
   */

  /**
   * Task 9:
   *
   * Write a function that computes the sum of the non-None values in a `List[Option[Int]]`.
   * Don't use a match statement.
   */

  /**
   * Task 10:
   *
   * Write a function that composes two functions of type `Double => Option[Double]`, yielding
   * another function of the same type. The composition should yield `None` if either function does.
   * For example,
   * {{{
   *  def f(x: Double) = if (x >= 0) Some(sqrt(x)) else None
   *  def g(x: Double) = if (x != 1) Some(1 / (x - 1)) else None
   *  val h = compose(f, g)
   * }}}
   * Then `h(2)` is `Some(1)`, and `h(1)` and `h(0)` are `None`.
   */

}

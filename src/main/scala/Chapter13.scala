import scala.collection.{concurrent, mutable}
import scala.collection.mutable.{LinkedList, ListBuffer, Map => mMap, Set => mSet}
import scala.io.Source

object Chapter13 {

  /**
   * Task 1:
   *
   * Write a function that, given a string, produces a map of the indexes of all characters.
   * For example, `indexes("Mississippi")` should return a map associating
   * 'M' with the set {0},
   * ‘i’ with the set {1, 4, 7, 10}, and so on.
   * Use a mutable map of characters to mutable sets. How can you ensure that the set is sorted?
   *
   * Solution:
   * You can use a SortedSet to make sure the set is sorted, but I don't think it matters because
   * a string's index increases monotonically.
   */
  def indexes(str: String): mMap[Char, mSet[Int]] = {
    val myMap = mMap[Char, mSet[Int]]()
    (0 until str.length).foreach(i => {
      myMap(str(i)) = myMap.get(str(i)).map(_ ++ mSet(i)).getOrElse(mSet(i))
    })
    myMap
  }

  /**
   * Task 2:
   *
   * Repeat the preceding exercise, using an immutable map of characters to lists.
   *
   * Alternative option (gives you a Map[Char, IndexedSeq[Int]):
   * (0 until str.length).groupBy(str(_))
   */
  def indexes2(str: String): Map[Char, List[Int]] = {
    str.toCharArray
      .zipWithIndex
      .foldLeft(Map[Char, List[Int]]())((acc: Map[Char, List[Int]], curr: (Char, Int)) => {
        val updatedIndexes = acc.get(curr._1).map(_ :+ curr._2).getOrElse(List(curr._2))
        acc.updated(curr._1, updatedIndexes)
      })
  }

  /**
   * Task 3:
   *
   * Write a function that removes all zeroes from a linked list of integers.
   */

//  def removeAllZeroes1(l: LinkedList[Int]): LinkedList[Int] = {
//    if (l.isEmpty) { return l }
//    if (l.head == 0) removeAllZeroes(l.tail) else l.head +: removeAllZeroes(l.tail)
//  }
//  def removeAllZeroes2(l: LinkedList[Int]): LinkedList[Int] = l.filter(_ != 0)

    def removeAllZeroes(l: LinkedList[Int]): LinkedList[Int] = {
      var myList = l
      if (myList.isEmpty) return myList
      (myList.elem, removeAllZeroes(myList.next)) match {
        case (0, t) if t.isEmpty =>
          myList = t
        case (0, t) =>
          myList.elem = t.elem
          myList.next = t.next
        case (_, t) =>
          myList.next = t
      }
      myList
    }

  /**
   * Task 3a:
   *
   * Write a function that removes every second element from a `ListBuffer`
.  * Try it two ways. Call `remove(i)` for all even `i` starting at the end of the list.
   * Copy every second element to a new list. Compare the performance.
   *
   * Solution:
   *
   * According to the docs, ListBuffer offers constant time prepend and append, but most other
   * implementations are linear. Therefore, we can assume that `remove` is O(n).
   *
   * That means that the first way (with `remove`) results in an O(n^2) runtime, but the second
   * way (which relies on `append`) is O(n).
   */
  def removeEveryOtherElement[A](lb: ListBuffer[A]): ListBuffer[A] = {
    // O(n^2)
    for (i <- lb.length-1 to 0 by -1 if i % 2 == 0) {
      // O(n)
      lb.remove(i)
    }
    lb
  }

  def removeEveryOtherElement2[A](lb: ListBuffer[A]): ListBuffer[A] = {
    val everyOtherElement = new ListBuffer[A]()
    // O(n)
    for (i <- lb.length-1 to 0 by -1 if i % 2 != 0) {
      // O(1)
      everyOtherElement.append(lb(i))
    }
    everyOtherElement
  }


  /**
   * Task 4:
   *
   * Write a function that receives a collection of strings and a map from strings to integers.
   * Return a collection of integers that are values of the map corresponding to one of
   * the strings in the collection. For example, given
   * `Array("Tom", "Fred", "Harry")` and `Map("Tom" -> 3, "Dick" -> 4, "Harry" -> 5)`,
   * return `Array(3, 5)`. Hint: Use flatMap to combine the Option values returned by get.
   */
  def mapToValues(coll: Array[String], sMap: Map[String, Int]): Array[Int] = {
    coll.flatMap(sMap.get)
  }

  /**
   * Task 5:
   *
   * Implement a function that works just like `mkString`, using `reduceLeft`.
   */
  def collToString[A](coll: Iterable[A], sep: String): String = {
    if (coll.isEmpty) return ""
    coll.reduceLeft((a: Any, b: A) => {
      a + sep + b
    }).toString
  }

  /**
   * Task 6:
   *
   * Given a list of integers `lst`, what is
   * `(lst :\ List[Int]())(_ :: _)` ?
   * `(List[Int]() /: lst)(_ :+ _)` ?
   * How can you modify one of them to reverse the list?
   *
   * Solution:
   * Both of these expressions return the same list again without doing anything.
   * `(lst :\ List[Int]())(_ :: _)` calls foldRight. It processes the list from right to left,
   * prepending each new element to the head of the list.
   * `(List[Int]() /: lst)(_ :+ _)` calls foldLeft. It processes the list from left to right,
   * appending each new element to the tail of the list.
   *
   * In Scala Lists, the append operation is linear but the prepend operation is constant time.
   * It's better to modify the second operation to use prepend.
   */
  // List has O(1) prepend but O(n) append
  def reverseList(lst: List[Int]): List[Int] = (List[Int]() /: lst)((b, a) => {
    a +: b
  })

  /**
   * Task 7:
   *
   * In Section 13.11, "Zipping", on page 171, the expression
   * {{{
   *  (prices zip quantities) map { p => p._1 * p._2 }
   * }}}
   * is a bit inelegant. We can't do
   * {{{
   *  (prices zip quantities) map { _ * _ }
   * }}}
   * because `_ * _` is a function with two arguments, and we need a function with one argument
   * that is a tuple. The `tupled` method of the `Function` object changes a function with
   * two arguments to one that take a tuple. Apply `tupled` to the multiplication function
   * so you can map it over the list of pairs.
   */
  def multiply(as: List[Int], bs: List[Int]): List[Int] = {
    as zip bs map {Function.tupled(_*_)}
//    as zip bs map {{(a: Int, b: Int) => {a*b}}.tupled}
  }

  /**
   * Task 8:
   *
   * Write a function that turns an array of `Double` values into a two-dimensional array.
   * Pass the number of columns as a parameter. For example, with `Array(1, 2, 3, 4, 5, 6)`
   * and three columns, return `Array(Array(1, 2, 3), Array(4, 5, 6))`. Use the `grouped` method.
   */

  /**
   * Task 9:
   *
   * The Scala compiler transforms a `for/yield` expression
   * {{{
   *   for (i <- 1 to 10; j <- 1 to i) yield i*j
   * }}}
   * to invocations of `flatMap` and `map`, like this:
   * {{{
   *   (1 to 10).flatMap(i => (1 to i).map(j => i * j))
   * }}}
   * Explain the use of `flatMap`. Hint: What is `(1 to i).map(j => i * j)` when `i` is `1,2,3`?
   * What happens when there are three generators in the `for/yield` expression?
   */


  /**
   * Task 10:
   * The method `java.util.TimeZone.getAvailableIDs` yields time zones such as `Africa/Cairo`
   * and `Asia/Chungking`. Which continent has the most time zones? Hint: `groupBy`.
   */

  /**
   * Task 11:
   *
   * Harry Hacker reads a file into a string and wants to use a parallel collection to update
   * the letter frequencies concurrently on portions of the string. He uses the following code:
   * {{{
   *  val frequencies = new scala.collection.mutable.HashMap[Char, Int]
   *  for (c <- str.par) frequencies(c) = frequencies.getOrElse(c, 0) + 1
   * }}}
   * Why is this a terrible idea? How can he really parallelize the computation?
   * (Hint: Use aggregate.)
   */

}

import java.util
import java.util.Scanner

import scala.collection.JavaConverters._
import java.util.Calendar
import scala.collection.mutable.{Map => mMap, SortedMap => mSortedMap, LinkedHashMap => mLinkedHashMap}
import scala.collection.mutable.ListBuffer

object Chapter04 {

  /**
   * Task 1:
   *   Set up a map of prices for a number of gizmos that you covet.
   *   Then produce a second map with the same keys and the prices at a 10 percent discount.
   */
  def gizmosWithReducedPrice(gizmos: Map[String, Double]): Map[String, Double] = {
    gizmos.mapValues(_ * 0.90)
  }

  /**
   * Task 2:
   *   Write a program that reads words from a file. Use a mutable map to count
   *   how often each word appears. To read the words, simply use a java.util.Scanner:
   * <blockquote><code>
   *     val in = new java.util.Scanner(new java.io.File("myfile.txt")) <br/>
   *     while (in.hasNext()) process in.next()
   * </code></blockquote>
   */
  def countWordsMutableMap(): mMap[String, Int] = {
    val in = new java.util.Scanner(getClass.getResourceAsStream("/resources/myfile.txt"))
    val wordCount = scala.collection.mutable.Map[String, Int]()
    while (in.hasNext()) {
      val word = in.next()
      wordCount(word) = wordCount.getOrElse(word, 0) + 1
    }
    wordCount
  }

  /**
   * Task 3:
   *   Repeat the preceding exercise with an immutable map.
   */
  def countWordsImmutableMap(): Map[String, Int] = {
    val in = new java.util.Scanner(getClass.getResourceAsStream("/resources/myfile.txt"))
    var wordCount = Map[String, Int]()
    while (in.hasNext()) {
      val word = in.next()
      val count = wordCount.getOrElse(word, 0) + 1
      wordCount += (word -> count)
    }
    wordCount
  }

  /**
   * Task 4:
   *   Repeat the preceding exercise with a sorted map,
   *   so that the words are printed in sorted order.
   */
  def countWordsSortedMap(): mSortedMap[String, Int] = {
    val in = new java.util.Scanner(getClass.getResourceAsStream("/resources/myfile.txt"))
    val wordCount = scala.collection.mutable.SortedMap[String, Int]()
    while (in.hasNext()) {
      val word = in.next()
      wordCount(word) = wordCount.getOrElse(word, 0) + 1
    }
    wordCount
  }

  /**
   * Task 5:
   *   Repeat the preceding exercise with a java.util.TreeMap
   *   that you adapt to the Scala API.
   */
  def countWordsTreeMap(): mMap[String, Int] = {
    val in = new java.util.Scanner(getClass.getResourceAsStream("/resources/myfile.txt"))
    val wordCount = new java.util.TreeMap[String, Int]().asScala
    while (in.hasNext()) {
      val word = in.next()
      wordCount(word) = wordCount.getOrElse(word, 0) + 1
    }
    wordCount
  }

  /**
   * Task 6:
   *   Define a linked hash map that maps "Monday" to java.util.Calendar.MONDAY,
   *   and similarly for the other weekdays. Demonstrate that the elements
   *   are visited in insertion order.
   */
  def weekdaysLinkedHashMap(): mLinkedHashMap[String, Int] = {
    mLinkedHashMap("Monday" -> Calendar.MONDAY,
      "Tuesday" -> Calendar.TUESDAY,
      "Wednesday" -> Calendar.WEDNESDAY,
      "Thursday" -> Calendar.THURSDAY,
      "Friday" -> Calendar.FRIDAY,
      "Saturday" -> Calendar.SATURDAY,
      "Sunday" -> Calendar.SUNDAY)
  }

  /**
   * Task 7:
   *   Print a table of all Java properties, like this:
   * <blockquote><code>
   *   java.runtime.name     | Java(TM) SE Runtime Environment <br/>
   *   sun.boot.library.path | /home/apps/jdk1.6.0_21/jre/lib/i386 <br/>
   *   java.vm.version       | 17.0-b16 <br/>
   *   java.vm.vendor        | Sun Microsystems Inc. <br/>
   *   java.vendor.url       | http://java.sun.com/ <br/>
   *   path.separator        | : <br/>
   *   java.vm.name          | Java HotSpot(TM) Server VM <br/>
   * </code></blockquote>
   *   You need to find the length of the longest key before you can print the table.
   */
  def formatJavaProperties(): List[String] = ???

  /**
   * Task 8:
   *   Write a function minmax(values: Array[Int]) that returns a pair containing
   *   the smallest and largest values in the array.
   */
  def minmax(values: Array[Int]): (Int, Int) = (values.min, values.max)

  /**
   * Task 9:
   *   Write a function lteqgt(values: Array[Int], v: Int) that returns a triple containing
   *   the counts of values less than v , equal to v , and greater than v.
   */
  def lteqgt(values: Array[Int], v: Int): (Int, Int, Int) =
    (values.count(_ < v), values.count(_ == v), values.count(_ > v))

//  def main(args: Array[String]) {
//    // task 2
//    println(countWordsMutableMap().mkString("\n"))
//
//    // task 7
//    println(formatJavaProperties().mkString("\n"))
//
//    // task 10
//    println("Hello".zip("World").mkString("\n"))
//  }
}

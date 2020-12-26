import java.awt.datatransfer.{DataFlavor, SystemFlavorMap}
import java.util.TimeZone

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.{Random, Sorting}

object Chapter03 {

  /**
   * Task 1:
   *   Write a code snippet that sets a to an array of n random integers
   *   between 0 (inclusive) and n (exclusive)
   */
  def randomIntArray(n: Int): Array[Int] =
    for (_ <- new Array[Int](n)) yield Random.nextInt(n)

  /**
   * Task 2:
   *   Write a loop that swaps adjacent elements of an array of integers.
   *   For example, Array(1, 2, 3, 4, 5) becomes Array(2, 1, 4, 3, 5)
   */
  def swapAdjacent(ary: Array[Int]): Array[Int] = {
    for (i <- ary.indices by 2 if i + 1 < ary.length) {
        val tmp = ary(i)
        ary(i) = ary(i+1)
        ary(i+1) = tmp
    }
    ary
  }

  /**
   * Task 3:
   *   Repeat the preceding assignment, but produce a new array
   *   with the swapped values. Use for/yield
   */
  def swapAdjacentYield(ary: Array[Int]): Array[Int] = {
    val swapped = for (i <- ary.indices ) yield {
      i match {
        case x if x % 2 == 0 && x+1 < ary.length => ary(x+1)
        case x if x % 2 != 0 => ary(x-1)
        case x => ary(x)
      }
    }
    swapped.toArray
  }

  /**
   * Task 4:
   *   Given an array of integers, produce a new array that contains
   *   all positive values of the original array, in their original order,
   *   followed by all values that are zero or negative,
   *   in their original order
   */
  def positivesThenNegatives(ary: Array[Int]): Array[Int] =
    ary.filter(_ > 0) ++ ary.filter(_ <= 0)

  /**
   * Task 5:
   *   How do you compute the average of an Array[Double] ?
   */
  def computeAverage(ary: Array[Double]): Double = ary.sum / ary.length

  /**
   * Task 6a:
   *   How do you rearrange the elements of an Array[Int]
   *   so that they appear in reverse sorted order?
   */
//  def reverseSortArray(ary: Array[Int]): Array[Int] = ary.sorted.reverse
  def reverseSortArray(ary: Array[Int]): Array[Int] = ary.sortWith(_ > _)

  /**
   * Task 6b:
   *   How do you rearrange the elements of an ArrayBuffer[Int]
   *   so that they appear in reverse sorted order?
   */
  def reverseSortArrayBuffer(ary: ArrayBuffer[Int]) = ary.sortWith(_ > _)

  /**
   * Task 7:
   *   Write a code snippet that produces all values from an array with duplicates removed.
   *   (Hint: Look at Scaladoc.)
   */
  def removeDuplicates(ary: Array[Int]): Array[Int] = ary.distinct

  /**
   * Task 8a:
   * Suppose you are given an array buffer of integers and want to remove all but the first
   * negative number. Here is a sequential solution that sets a flag when the first negative
   * number is called, then removes all elements beyond.
   *
   * {{{
   *   var first = true
   *   var n = a.length
   *   var i = 0
   *   while (i < n) {
   *     if (a(i) >= 0) i += 1
   *     else {
   *       if (first) { first = false; i +=1 }
   *       else { a.remove(i); n -= 1 }
   *     }
   *   }
   * }}}
   *
   * This is a complex and inefficient solution. Rewrite it in Scala by collecting positions of
   * the negative elements, dropping the first element, reversing the sequence, and calling
   * `a.remove(i)` for each index.
   */
  def dropNegativesExceptFirst(ary: ArrayBuffer[Int]): ArrayBuffer[Int]= {
    val negativeElements = for (i <- ary.indices if ary(i) < 0) yield i
    for (i <- negativeElements.drop(1).reverse) ary.remove(i)
    ary
  }

  /**
   * Task 8b:
   * Improve the solution of the preceding exercise by collecting the positions that should be moved
   * and their target positions. Make those moves and truncate the buffer. Don't copy any elements
   * before the first unwanted element.
   */
  def dropNegativesV2(ary: ArrayBuffer[Int]): ArrayBuffer[Int] =  ???

  /**
   * Task 10:
   *   Make a collection of all time zones returned by java.util.TimeZone.getAvailableIDs
   *   that are in America. Strip off the "America/" prefix and sort the result.
   */
  def americaTimeZones: Array[String] = {
    java.util.TimeZone.getAvailableIDs()
      .filter(_.contains("America"))
      .map(_.replace("America/", ""))
      .sorted
  }

  /**
   * Task 11:
   *   Import java.awt.datatransfer._ and make an object of type SystemFlavorMap with the call
   *
   *   val flavors = SystemFlavorMap.getDefaultFlavorMap().asInstanceOf[SystemFlavorMap]
   *
   *   Then call the getNativesForFlavor method with parameter DataFlavor.imageFlavor
   *   and get the return value as a Scala buffer. (Why this obscure class? It’s hard
   *   to find uses of java.util.List in the standard Java library.)
   */

}

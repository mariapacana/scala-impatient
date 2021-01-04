package Chapter17Tasks

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.io.StdIn.readLine
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Task 6:
 *
 * Write a method
 *
 * {{{
 *   Future[T] repeat(action: => T, until: T => Boolean)
 * }}}
 *
 * that asynchronously repeats the action until it produces a value that is accepted by the `until`
 * predicate, which should also run asynchronously. Test with a function that reads a password from
 * the console, and a function that simulates a validity check by sleeping for a second and then
 * checking that the password is "`secret`". Hint: Use recursion.
 * */

object Chapter17Task06 extends App {
  def repeat[T](action: => T, until: T => Boolean): Future[T] = {
    val futureAction = Future { action }
    val futureUntil = Future { until }

    for {
      a <- futureAction
      u <- futureUntil
      r <- if (!(u(a))) repeat(action, until) else Future { a }
    } yield r
//
//    futureAction.flatMap(r => {
//      futureUntil.flatMap(u => {
//        if (!u(r)) repeat(action, until) else Future { r }
//      })
//    })
  }

  def u: String => Boolean = p => {
    Thread.sleep(1000);
    println(s"Password was : ${p}")
    p == "secret"
  }

  def readPassword: String = {
    println("Type in your password!")
    readLine()
  }

  val result = Await.result(repeat(readPassword, u), 1000.seconds)
}

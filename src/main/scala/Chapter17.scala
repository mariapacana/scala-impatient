import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.immutable.Nil

object Chapter17 {

  /**
   * Task 1:
   *
   * Consider the expression
   * {{{
   *   for (n1 <- Future { Thread.sleep(1000) ; 2 }
   *   n2 <- Future { Thread.sleep(1000); 40})
   *   println(n1 + n2)
   * }}}
   *
   * How is the expression translated to `map` and `flatMap` calls? Are the two futures executed
   * concurrently or one after the other? In which thread does the call to `println` occur?
   */

  /**
   * Task 2:
   *
   * Write a function `doInOrder` that, given two functions `f: T => Future[U]` and
   * `g: U => Future[V]`, produces a function `T => Future[U]` that, for a given `t`, eventually
   * yields `g(f(t))`.
   */
  def doInOrder[U, T, V](f: T => Future[U], g: U => Future[V]): T => Future[V] =
    (t: T) => for {
      u <- f(t)
      v <- g(u)
    } yield v

  def doInOrder2[U, T, V](f: T => Future[U], g: U => Future[V]): T => Future[V] =
    (t: T) => f(t).flatMap(g)

  /**
   * Task 3:
   *
   * Repeat the preceding exercise for any sequence of functions of type `T => Future[T]`.
   */
  def doInOrderSequence[T](s: Seq[T => Future[T]]): T => Future[T] =
    s.reduce((acc, f) => (t: T) => acc(t).flatMap(f))

  def doInOrderSequence2[T](s: Seq[T => Future[T]]): T => Future[T] = {
    t: T => s match {
      case head::Nil => head(t)
      case head::tail => {
        head(t).flatMap(doInOrderSequence(tail))
      }
    }
  }

  /**
   * Task 4:
   *
   * Write a function `doTogether` that, given two functions `f: T => Future[U]` and
   * `g: U => Future:[V]`, produces a function `T => Future[(U, V)]`, running the two computations
   * in parallel and, for a given `t`, eventually yielding `(f(t), g(t))`.
   *
   * Note:
   * This is the best solution I could come up with, but it doesn't always seem to run
   * the tasks in parallel. The tasks are on the same thread 25% of the time.
   * "You can enforce sequential execution or ALLOW for parallel execution."
   */
  def doTogether[T,U,V](f: T => Future[U], g: T => Future[V]): T => Future[(U,V)] = {
    (t: T) => {
      // Need to take these computations OUTSIDE the `flatMap` calls in order to run them
      // in parallel. Putting the computations inside the `flatMap` forces sequential execution.
      val f1 = f(t)
      val g1 = g(t)
      for {
        u <- f1
        v <- g1
      } yield (u, v)
    }
  }

  /**
   * Task 5:
   *
   * Write a function that receives a sequence of futures and returns a future that eventually
   * yields a sequence of all results.
   */
  def futureSequence[T](s: Seq[Future[T]]) = {

  }

}

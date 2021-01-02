import org.scalatest.{FlatSpec, Matchers}
import Chapter17._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Success

class Chapter17Spec extends FlatSpec with Matchers {

  "doInOrder" should "compose two functions" in {
    val a = (x: Int) => Future { 3*x }
    val b = (x: Int) => Future { x - 2 }

    val result = Await.result(doInOrder(a, b)(1),10.seconds)
    result shouldBe 1
  }

  "doInOrder2" should "compose two functions" in {
    val a = (x: Int) => Future { 3*x }
    val b = (x: Int) => Future { x - 2 }

    val result = Await.result(doInOrder2(a, b)(1),10.seconds)
    result shouldBe 1
  }

  "doInOrderSequence" should "apply a sequence of functions in order" in {
    val a = (x: Int) => Future { 3*x }
    val b = (x: Int) => Future { x - 2 }
    val c = (x: Int) => Future { x * 5 }

    val result = Await.result(doInOrderSequence[Int](Seq(a,b,c))(5),10.seconds)
    result shouldBe 65
  }

  "doInOrderSequence2" should "apply a sequence of functions in order" in {
    val a = (x: Int) => Future { 3*x }
    val b = (x: Int) => Future { x - 2 }
    val c = (x: Int) => Future { x * 5 }

    val result = Await.result(doInOrderSequence2[Int](Seq(a,b,c))(5),10.seconds)
    result shouldBe 65
  }

  "doTogether" should "run two computations in parallel" in {
    val result = Await.result(doTogether[Int, Int, Int](task, task)(2), 10.seconds)

    result shouldBe (4, 4)
  }

  "futureSequence" should "receive a sequence of futures and return a sequence of all results" in {
    val result = Await.result(futureSequence(Seq(task(1), task(2), task(3))), 10.seconds)

    result shouldBe Seq(1, 4, 9)
  }

  "futureSequence2" should "receive a sequence of futures and return a sequence of all results" in {
    val result = Await.result(futureSequence2(Seq(task(1), task(2), task(3))), 10.seconds)

    result shouldBe Seq(1, 4, 9)
  }

  def task(i: Int): Future[Int] = {
    Future {
      println(s"task=$i thread=${Thread.currentThread().getId} time=${System.currentTimeMillis()}")
      i * i
    }
  }
}

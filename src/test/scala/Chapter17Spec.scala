import org.scalatest.{FlatSpec, Matchers}
import Chapter17._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

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

}

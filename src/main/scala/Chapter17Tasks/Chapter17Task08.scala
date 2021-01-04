import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.io.StdIn.readLine
import scala.io.{Codec, Source}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Task 8:
 *
 * Write a program that asks the user for a URL, reads the web page at that URL, and displays all the
 * hyperlinks. Use a separate `Future` for each of these three steps.
 */
object Chapter17Task08 extends App {

  implicit val codec: Codec = Codec.ISO8859

  def getURL: String = {
    println("Give me a URL!")
    readLine()
  }

  def readURL(url: String): String = {
    Source.fromURL(url).mkString
  }

  def getHyperlinks(contents: String): List[String] = ???

  val myUrl = getURL
  println(s"url = ${myUrl}")
  val contents = readURL(myUrl)
  println(contents)
}

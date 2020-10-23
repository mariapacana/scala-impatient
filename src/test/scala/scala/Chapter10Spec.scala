import Chapter10._
import java.beans.{PropertyChangeEvent, PropertyChangeListener}
import java.io.{FilterInputStream, InputStream}
import org.scalatest.{FlatSpec, Matchers}
import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

class Chapter10Spec extends FlatSpec with Matchers {

  "RectangleLike" should "provide translate and grow methods" in {
    //given
    val egg = new java.awt.geom.Ellipse2D.Double(5, 10, 20, 30) with RectangleLike

    //when & then
    egg.translate(10, -10)
    egg.getX shouldBe 15
    egg.getY shouldBe 0
    egg.getWidth shouldBe 20
    egg.getHeight shouldBe 30

    //when & then
    egg.grow(10, 20)
    egg.getX shouldBe 5
    egg.getY shouldBe -20
    egg.getWidth shouldBe 40
    egg.getHeight shouldBe 70
  }

  "OrderedPoint" should "use lexicographic ordering" in {
    //when & then
    new OrderedPoint(1, 1).compare(new OrderedPoint(2, 2)) shouldBe -1
    new OrderedPoint(1, 1).compare(new OrderedPoint(2, 1)) shouldBe -1
    new OrderedPoint(1, 1).compare(new OrderedPoint(1, 2)) shouldBe -1
    new OrderedPoint(1, 1).compare(new OrderedPoint(1, 1)) shouldBe 0
    new OrderedPoint(1, 1).compare(new OrderedPoint(0, 1)) shouldBe 1
    new OrderedPoint(1, 1).compare(new OrderedPoint(1, 0)) shouldBe 1
    new OrderedPoint(1, 1).compare(new OrderedPoint(0, 0)) shouldBe 1
  }

  "bitSetLinearization" should "return linearization of BitSet traits" in {
    //when
    val result: List[String] = bitSetLinearization

    //then
    // got from here:
    //  http://stackoverflow.com/questions/15623498/handy-ways-to-show-linearization-of-a-class
    val tpe = scala.reflect.runtime.universe.typeOf[scala.collection.BitSet]
    result shouldBe tpe.baseClasses.map(_.fullName)
  }

  "CryptoLogger" should "encrypt messages with the Caesar cipher default key" in {
    //given
    val logger = new TestLogger with CryptoLogger

    //when
    logger.log("12345")

    //then
    logger.message shouldBe "45678"
  }

  it should "encrypt messages with the Caesar cipher key -3" in {
    //given
    val logger = new TestLogger with CryptoLogger {
      override val key = -3
    }

    //when
    logger.log("45678")

    //then
    logger.message shouldBe "12345"
  }

  "PointBean" should "extend java.awt.Point with PropertyChangeSupportLike" in {
    //given
    val point = new PointBean

    //when
    var event: PropertyChangeEvent = null
    point.addPropertyChangeListener("x", new PropertyChangeListener {
      override def propertyChange(evt: PropertyChangeEvent): Unit = {
        event = evt
      }
    })
    point.x += 1
    point.firePropertyChange("x", 0, 1)

    //then
    point.hasListeners("x") shouldBe true
    point.hasListeners("y") shouldBe false
    event.getPropertyName shouldBe "x"
    event.getOldValue shouldBe 0
    event.getNewValue shouldBe 1
  }

  "TestEngine" should "extend Engine with InfiniteEngine" in {
    //given
    val engine = new TestEngine

    //when
    engine.start()
    engine.stop()

    //then
    engine.model shouldBe "test"
  }

  "BufferedInputStreamLike" should "add buffering to an input stream" in {
    //given
    val in = new {
      override val bufferSize = 48
    } with FilterInputStream(getClass.getResourceAsStream("/myfile.txt"))
      with BufferedInputStreamLike
      with ConsoleLogger

    try {
      //when
      val result = Source.fromBytes(readBytes(in)).mkString

      //then
      result shouldBe """
                        |Simple text file with example words.
                        |We will parse the file and count the words.
                        |""".stripMargin
    }
    finally {
      in.close()
    }
  }

  "IterableInputStream" should "extends java.io.InputStream with the trait Iterable[Byte]" in {
    //given
    val in = new IterableInputStream(getClass.getResourceAsStream("/myfile.txt"))

    try {
      //when
      val result = new ArrayBuffer[Byte]
      result ++= in

      //then
      new String(result.toArray) shouldBe """
                                            |Simple text file with example words.
                                            |We will parse the file and count the words.
                                            |""".stripMargin
    }
    finally {
      in.close()
    }
  }

  private def readBytes(in: InputStream): Array[Byte] = {
    val buf = new ArrayBuffer[Byte]

    @tailrec
    def read(): Unit = {
      val byte = in.read()
      if (byte == -1) return

      buf += byte.toByte
      read()
    }

    read()
    buf.toArray
  }

  class TestLogger extends Logger {
    var message = ""
    override def log(msg: String): Unit = message = msg
  }
}

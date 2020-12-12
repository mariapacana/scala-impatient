import Chapter10._
import task1004.{CryptoLogger, Logger => MyLogger}
import task1008.{HybridCar, Vehicle, Car => MyCar}
import java.beans.{PropertyChangeEvent, PropertyChangeListener}
import java.io.{File, FileInputStream, FilterInputStream, InputStream}
import java.util.logging.Logger

import Chapter10.Chapter1009.BufferedInputStreamLike
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
    egg.getX shouldBe 15.0
    egg.getY shouldBe 0.0
    egg.getWidth shouldBe 20.0
    egg.getHeight shouldBe 30.0

    //when & then
    egg.grow(10, 20)
    egg.getX shouldBe 15.0
    egg.getY shouldBe 0.0
    egg.getWidth shouldBe 40.0
    egg.getHeight shouldBe 70.0
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

//  "bitSetLinearization" should "return linearization of BitSet traits" in {
//    //when
//    val result: List[String] = bitSetLinearization
//
//    //then
//    // got from here:
//    //  http://stackoverflow.com/questions/15623498/handy-ways-to-show-linearization-of-a-class
//    val tpe = scala.reflect.runtime.universe.typeOf[scala.collection.BitSet]
//    result shouldBe tpe.baseClasses.map(_.fullName)
//  }

  "CryptoLogger" should "encrypt messages with the Caesar cipher default key" in {
      //given
      val logger = new TestLogger with CryptoLogger

      logger.log("abcd")
      logger.message shouldBe("defg")
  }

  it should "encrypt messages with the Caesar cipher key -3" in {
    //given
    val logger = new TestLogger with CryptoLogger {
      override val key = -3
    }

    //when
    logger.log("defg")

    //then
    logger.message shouldBe "abcd"
  }
//
//  "PointBean" should "extend java.awt.Point with PropertyChangeSupportLike" in {
//    //given
//    val point = new PointBean
//
//    //when
//    var event: PropertyChangeEvent = null
//    point.addPropertyChangeListener("x", new PropertyChangeListener {
//      override def propertyChange(evt: PropertyChangeEvent): Unit = {
//        event = evt
//      }
//    })
//    point.x += 1
//    point.firePropertyChange("x", 0, 1)
//
//    //then
//    point.hasListeners("x") shouldBe true
//    point.hasListeners("y") shouldBe false
//    event.getPropertyName shouldBe "x"
//    event.getOldValue shouldBe 0
//    event.getNewValue shouldBe 1
//  }

  "Car" should "increase the fuel level in a car with fillTank" in {
    //given
    val camry = new MyCar
    //when
    camry.fillTank(5.0)
    //then
    camry.tankLevel shouldBe 5.0
  }

  it should "not be filled beyond maximum capacity" in {
    //given
    val camry = new MyCar
    //when
    camry.fillTank(100.0)
    //then
    camry.tankLevel shouldBe 10.0
  }

  it should "reduce the tank level when you drive it" in {
    //given
    val camry = new MyCar
    //when
    camry.fillTank(10.0)
    val milesLeft = camry.drive(60)
    //then
    milesLeft shouldBe(0.0)
    camry.tankLevel shouldBe 8.0
  }

  "Hybrid Car" should "rely on the battery first and not use gas" in {
    //given
    val camry = new HybridCar
    //when
    camry.charge(12)
    camry.fillTank(10.0)
    val milesLeft = camry.drive(60)
    //then
    milesLeft shouldBe(0.0)
    camry.batteryVoltage shouldBe(6.0)
    camry.tankLevel shouldBe 10.0
  }

  "Hybrid Car" should "use gas after the battery is exhausted" in {
    //given
    val camry = new HybridCar
    //when
    camry.charge(12)
    camry.fillTank(10.0)
    val milesLeft = camry.drive(200)
    //then
    milesLeft shouldBe(0.0)
    camry.batteryVoltage shouldBe(0.0)
    camry.tankLevel shouldBe(7.33 +- 0.01)
  }


  "BufferedInputStreamLike" should "add buffering to an input stream" in {
    //given
    val in = new FileInputStream(new File(getClass.getResource("/resources/myfile.txt").getPath))
      with BufferedInputStreamLike

    try {
      //when
      var result = ""
      var a = in.read()
      while (a != -1) {
        result = result + a.toChar
        a = in.read()
      }

      //then
      result shouldBe "Simple text file with example words.\nWe will parse the file and count the words.\n"
    }
    finally {
      in.close()
    }
  }

  "IterableInputStream hasNext" should "be true when there's more input and false when there's no input left" in {
    //given
    val in = new IterableInputStream(getClass.getResourceAsStream("/resources/myfile.txt"))
    val it = in.iterator

    try {
      it.hasNext shouldBe true

      //when
      val result = new ArrayBuffer[Byte]
      result ++= in

      //then
      it.hasNext shouldBe false
    }
    finally {
      in.close()
    }
  }

  "IterableInputStream next" should "provide the next byte in the stream" in {
    //given
    val in = new IterableInputStream(getClass.getResourceAsStream("/resources/myfile.txt"))
    val it = in.iterator

    try {
      it.next().toChar shouldBe 'S'
      it.next().toChar shouldBe 'i'
      it.next().toChar shouldBe 'm'
      it.next().toChar shouldBe 'p'
      it.next().toChar shouldBe 'l'
      it.next().toChar shouldBe 'e'

      val result = new ArrayBuffer[Byte]
      result ++= in
      new String(result.toArray) shouldBe " text file with example words.\nWe will parse the file and count the words.\n"
    }
    finally {
      in.close()
    }
  }

  "Logger" should "log" in {
    import Example.{SavingsAccount, SavingsAccount2}
    new SavingsAccount().withdraw(10.0)
    new SavingsAccount2().withdraw(10.0)
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

  class TestLogger extends MyLogger {
    var message = ""
    def log(msg: String): Unit = message = msg
  }

}

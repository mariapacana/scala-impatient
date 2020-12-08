import java.awt.{Point => JavaPoint}
import java.beans.{PropertyChangeEvent, PropertyChangeListener, PropertyChangeSupport}
import java.io.InputStream
import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

package Chapter10 {

  /**
   * Task 1:
   *
   * The `java.awt.Rectangle` class has useful methods `translate` and `grow`
   * that are unfortunately absent from classes such as `java.awt.geom.Ellipse2D`.
   * In Scala, you can fix this problem.
   * Define a trait `RectangleLike` with methods `translate` and `grow`. Provide any abstract
   * methods that you need for the implementation so that you can mix in the trait like this:
   * {{{
   *    val egg = new java.awt.geom.Ellipse2D.Double(5, 10, 20, 30) with RectangleLike
   *    egg.translate(10, -10)
   *    egg.grow(10, 20)
   * }}}
   */

  trait RectangleLike {
    def getX: Double
    def getY: Double
    def getWidth: Double
    def getHeight: Double
    def setFrame(x: Double, y: Double, w: Double, h: Double): Unit

    def translate(dx: Int, dy: Int) {
      setFrame(getX + dx, getY + dy, getWidth, getHeight)
    }

    def grow(dw: Int, dh: Int) {
      setFrame(getX, getY, getWidth + dw*2, getHeight + dh*2)
    }

  }


  /**
   * Task 2:
   *
   * Define a class `OrderedPoint` by mixing `scala.math.Ordered[Point]` into `java.awt.Point`.
   * Use lexicographic ordering, i e. (x, y) < (x’, y’) if x < x’ or x = x’ and y < y’.
   */
  class OrderedPoint(x: Int, y: Int) extends JavaPoint(x, y) with scala.math.Ordered[JavaPoint] {
    def compare(that: JavaPoint): Int = {
      ((this.x - that.x).signum, (this.y - that.y).signum) match {
        case (-1, _) | (_, -1) => -1
        case (0, 0) => 0
        case _ => 1
      }
    }
  }

  /**
   * Task 3:
   *
   * Look at the BitSet class, and make a diagram of all its superclasses and traits.
   * Ignore the type parameters (everything inside the [...]).
   * Then give the linearization of the traits.
   */

  /**
   * Task 4:
   *
   * Provide a `CryptoLogger` trait that encrypts the log messages with the Caesar cipher.
   * The key should be 3 by default, but it should be overridable by the user.
   * Provide usage examples with the default key and a key of -3.
   */
  package task1004 {
    trait Logger {
      def log(msg: String): Unit
    }

    trait CryptoLogger extends Logger {
      val key: Int = 3

      abstract override def log(msg: String): Unit = super.log(msg.map(i => (i.toInt + key).toChar))
    }
  }

  /**
   * Task 5:
   *
   * The JavaBeans specification has the notion of a property change listener, a standardized
   * way for beans to communicate changes in their properties. The `PropertyChangeSupport` class
   * is provided as a convenience superclass for any bean that wishes to support property change
   * listeners. Unfortunately, a class that already has another superclass - such as JComponent -
   * must reimplement the methods. Reimplement `PropertyChangeSupport` as a trait,
   * and mix it into the `java.awt.Point` class.
   */

  /**
   * Task 6:
   *
   * In the Java AWT library, we have a class `Container`, a subclass of `Component` that collects
   * multiple components. For example, a `Button` is a `Component`, but a `Panel` is a `Container`.
   * That's the composite pattern at work. Swing has `JComponent` and `JButton`, but if you look
   * closely, you will notice something strange. `JComponent` extends `Container`, even though
   * it makes no sense to add other components to say, a `JButton`.
   * The Swing designers would have ideally preferred the design in Figure 10-4:
   * {{{
   *  JButton --------> JComponent -------> Component
   *                          ^                  ^
   *                          |                  |
   *  JPanel ---> JContainer -----> Container ---+
   * }}}
   * But that's not possible in Java. Explain why not.
   * How could the design be executed in Scala with traits?
   *
   * In Java, a class can't inherit from more than one superclass. JContainer couldn't inherit
   * from both JComponent and Container. (Though why couldn't they have made Container an interface?)
   * In Scala, you can create a Container trait and mix it into JComponent to create JContainer.
   */

  /**
   * Task 7:
   *
   * Construct an example where a class needs to be recompiled when one of the mixins changes. Start with
   * `class SavingsAccount extends Account with ConsoleLogger`. Put each class and trait in a separate
   * source file. Add a field to `Account`. In `Main` (also in a separate source file), construct a `SavingsAccount`
   * and access the new field. Recompile all fields except for `SavingsAccount` and verify that the program
   *   works. Now add a field to `ConsoleLogger` and access it in `Main`. Again, recompile all files except for
   * `SavingsAccount`. What happens? Why?
   *
   * I was able to access the new fields both times. As long as you recompile `ConsoleLogger`, `Main` is
   * able to access the new field. There was no need to recompile `SavingsAccount` because it is just an
   * abstract class and we didn't change it.
   *
   */

  /**
   * Task 8:
   *
   * There are dozens of Scala trait tutorials with silly examples of barking dogs or
   * philosophizing frogs. Reading through contrived hierarchies can be tedious and not very
   * helpful, but designing your own is very illuminating. Make your own silly trait hierarchy
   * example that demonstrates layered traits, concrete and abstract methods, and concrete and
   * abstract fields.
   */
  package task1008 {
    trait Vehicle {
      var tankLevel = 0.0
      protected val tankCapacity: Double
      protected val mpg: Double

      def fillTank(gas: Double): Double = {
        tankLevel = if (gas + tankLevel <= tankCapacity) tankLevel + gas else tankCapacity
        tankLevel
      }

      def drive(miles: Double): Double
    }

    class Car extends Vehicle {
      override val tankCapacity = 10.0 // concrete field overrides abstract field maxTankLevel
      override val mpg = 30.0

      // concrete method overrides abstract drive method
      override def drive(miles: Double): Double = {
        var milesLeft = miles - tankLevel * mpg
        if (milesLeft <= 0) {
          tankLevel = tankLevel - miles / mpg
          milesLeft = 0
        }
        milesLeft
      }
    }

    /**
     * Assume that battery capacity is measured purely in volts.
     */
    trait Hybrid {
      var batteryVoltage = 0.0
      val batteryCapacity = 12.0
      val chargePerHour = 1.0
      val milesPerVolt = 10.0

      def charge(hours: Int): Double = {
        batteryVoltage = if (batteryCapacity > hours*chargePerHour) batteryCapacity else hours*chargePerHour
        batteryVoltage
      }
    }

    class HybridCar extends Car with Hybrid {
      override def drive(miles: Double): Double = {
        var milesLeft = miles - batteryVoltage * milesPerVolt
        if (milesLeft <= 0) {
          batteryVoltage = batteryVoltage - miles / milesPerVolt
          milesLeft = 0
        } else {
          batteryVoltage = 0.0
          milesLeft = super.drive(milesLeft)
        }
        milesLeft
      }
    }
  }

  /**
   * Task 9:
   *
   * In the `java.io` library, you add buffering to an input stream with a `BufferedInputStream`
   * decorator. Reimplement buffering as a trait. For simplicity, override the `read` method.
   */
  trait BufferedInputStreamLike extends InputStream {
    val bufferSize = 48
    var buffer = new Array[Byte](bufferSize)
    var pos = 0

    override def read(): Int = {
      if (bufferIsEmpty() || pos >= bufferSize) {
        this.read(buffer) // read input here
        pos = 0
      }
      val result = if (bufferIsEmpty()) -1 else {
        val r = buffer(pos)
        buffer(pos) = 0
        r
      }
      pos += 1
      result
    }

    def bufferIsEmpty(): Boolean = {
      buffer.forall(_ == 0)
    }
  }

  /**
   * Task 10:
   *
   * Using the logger traits from this chapter, add logging to the solution of the preceding
   * problem that demonstrates buffering.
   */

  /**
   * Task 11:
   *
   * Implement a class `IterableInputStream` that extends `java.io.InputStream` with the trait
   * `Iterable[Byte]`.
   */

  /**
   * Task 12:
   *
   * Using `javap -c -private`, analyze how the call `super.log(msg)` is translated to Java. How does the
   * same call invoke two different methods, depending on the mixin order?
   */

}

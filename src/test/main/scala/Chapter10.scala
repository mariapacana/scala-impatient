import java.awt.Point
import java.beans.{PropertyChangeEvent, PropertyChangeListener, PropertyChangeSupport}
import java.io.InputStream
import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object Chapter10 {

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

    def translate(dx: Double, dy: Double): Unit = {
      var x = getX
      var y = getY
      var width = getWidth
      var height = getHeight

      var oldv = x
      var newv = oldv + dx
      if (dx < 0) {
        if (newv > oldv) {
          if (width >= 0) {
            width += newv - Integer.MIN_VALUE
          }
          newv = Integer.MIN_VALUE
        }
      }
      else {
        if (newv < oldv) {
          if (width >= 0) {
            width += newv - Integer.MAX_VALUE
            if (width < 0) width = Integer.MAX_VALUE
          }
          newv = Integer.MAX_VALUE
        }
      }
      x = newv
      oldv = y
      newv = oldv + dy
      if (dy < 0) {
        if (newv > oldv) {
          if (height >= 0) {
            height += newv - Integer.MIN_VALUE
          }
          newv = Integer.MIN_VALUE
        }
      }
      else {
        if (newv < oldv) {
          if (height >= 0) {
            height += newv - Integer.MAX_VALUE
            if (height < 0) height = Integer.MAX_VALUE
          }
          newv = Integer.MAX_VALUE
        }
      }
      y = newv

      setFrame(x, y, width, height)
    }

    def grow(h: Double, v: Double): Unit = {
      var x0 = getX
      var y0 = getY
      var x1 = getWidth
      var y1 = getHeight
      x1 += x0
      y1 += y0
      x0 -= h
      y0 -= v
      x1 += h
      y1 += v
      if (x1 < x0) {
        x1 -= x0
        if (x1 < Integer.MIN_VALUE) x1 = Integer.MIN_VALUE
        if (x0 < Integer.MIN_VALUE) x0 = Integer.MIN_VALUE
        else if (x0 > Integer.MAX_VALUE) x0 = Integer.MAX_VALUE
      }
      else {
        if (x0 < Integer.MIN_VALUE) x0 = Integer.MIN_VALUE
        else if (x0 > Integer.MAX_VALUE) x0 = Integer.MAX_VALUE
        x1 -= x0
        if (x1 < Integer.MIN_VALUE) x1 = Integer.MIN_VALUE
        else if (x1 > Integer.MAX_VALUE) x1 = Integer.MAX_VALUE
      }
      if (y1 < y0) {
        y1 -= y0
        if (y1 < Integer.MIN_VALUE) y1 = Integer.MIN_VALUE
        if (y0 < Integer.MIN_VALUE) y0 = Integer.MIN_VALUE
        else if (y0 > Integer.MAX_VALUE) y0 = Integer.MAX_VALUE
      }
      else {
        if (y0 < Integer.MIN_VALUE) y0 = Integer.MIN_VALUE
        else if (y0 > Integer.MAX_VALUE) y0 = Integer.MAX_VALUE
        y1 -= y0
        if (y1 < Integer.MIN_VALUE) y1 = Integer.MIN_VALUE
        else if (y1 > Integer.MAX_VALUE) y1 = Integer.MAX_VALUE
      }

      setFrame(x0, y0, x1, y1)
    }
  }

  /**
   * Task 2:
   *
   * Define a class `OrderedPoint` by mixing `scala.math.Ordered[Point]` into `java.awt.Point`.
   * Use lexicographic ordering, i e. (x, y) < (x’, y’) if x < x’ or x = x’ and y < y’.
   */
  class OrderedPoint(x: Int, y: Int) extends Point(x, y) with scala.math.Ordered[Point] {

    override def compare(that: Point): Int = {
      if (x < that.x || (x == that.x && y < that.y)) -1
      else if (x == that.x && y == that.y) 0
      else 1
    }
  }

  /**
   * Task 3:
   *
   * Look at the BitSet class, and make a diagram of all its superclasses and traits.
   * Ignore the type parameters (everything inside the [...]).
   * Then give the linearization of the traits.
   */
  def bitSetLinearization: List[String] = {
    val diagram = Map(
      "scala.collection.BitSet" -> List("scala.AnyRef",
        "scala.collection.SortedSet",
        "scala.collection.BitSetLike"),

      "scala.collection.SortedSet" -> List("scala.AnyRef",
        "scala.collection.Set",
        "scala.collection.SortedSetLike"),

      "scala.collection.Set" -> List("scala.AnyRef",
        "scala.Function1",
        "scala.collection.Iterable",
        "scala.collection.GenSet",
        "scala.collection.generic.GenericSetTemplate",
        "scala.collection.SetLike"),

      "scala.collection.SetLike" -> List("scala.AnyRef",
        "scala.collection.IterableLike",
        "scala.collection.GenSetLike",
        "scala.collection.generic.Subtractable",
        "scala.collection.Parallelizable"),

      "scala.collection.Parallelizable" -> List("scala.Any"),

      "scala.collection.generic.Subtractable" -> List("scala.AnyRef"),

      "scala.collection.GenSetLike" -> List("scala.AnyRef",
        "scala.collection.GenIterableLike",
        "scala.Function1",
        "scala.Equals",
        "scala.collection.Parallelizable"),

      "scala.Equals" -> List("scala.Any"),

      "scala.collection.GenTraversableOnce" -> List("scala.Any"),

      "scala.collection.IterableLike" -> List("scala.Any",
        "scala.Equals",
        "scala.collection.TraversableLike",
        "scala.collection.GenIterableLike"),

      "scala.collection.GenIterableLike" -> List("scala.Any",
        "scala.collection.GenTraversableLike"),

      "scala.collection.GenTraversableLike" -> List("scala.Any",
        "scala.collection.GenTraversableOnce",
        "scala.collection.Parallelizable"),

      "scala.collection.TraversableLike" -> List("scala.Any",
        "scala.collection.generic.HasNewBuilder",
        "scala.collection.generic.FilterMonadic",
        "scala.collection.TraversableOnce",
        "scala.collection.GenTraversableLike",
        "scala.collection.Parallelizable"),

      "scala.collection.TraversableOnce" -> List("scala.Any",
        "scala.collection.GenTraversableOnce"),

      "scala.collection.generic.FilterMonadic" -> List("scala.Any"),

      "scala.collection.GenSet" -> List("scala.AnyRef",
        "scala.collection.GenSetLike",
        "scala.collection.GenIterable",
        "scala.collection.generic.GenericSetTemplate"),

      "scala.collection.GenIterable" -> List("scala.AnyRef",
        "scala.collection.GenIterableLike",
        "scala.collection.GenTraversable",
        "scala.collection.generic.GenericTraversableTemplate"),

      "scala.collection.GenTraversable" -> List("scala.AnyRef",
        "scala.collection.GenTraversableLike",
        "scala.collection.GenTraversableOnce",
        "scala.collection.generic.GenericTraversableTemplate"),

      "scala.collection.generic.GenericSetTemplate" -> List("scala.AnyRef",
        "scala.collection.generic.GenericTraversableTemplate"),

      "scala.collection.generic.GenericTraversableTemplate" -> List("scala.AnyRef",
        "scala.collection.generic.HasNewBuilder"),

      "scala.collection.generic.HasNewBuilder" -> List("scala.Any"),

      "scala.Function1" -> List("scala.AnyRef"),

      "scala.AnyRef" -> List("scala.Any", "java.lang.Object"),
      "scala.Any" -> Nil,
      "java.lang.Object" -> Nil,

      "scala.collection.Iterable" -> List("scala.AnyRef",
        "scala.collection.Traversable",
        "scala.collection.GenIterable",
        "scala.collection.generic.GenericTraversableTemplate",
        "scala.collection.IterableLike"),

      "scala.collection.Traversable" -> List("scala.AnyRef",
        "scala.collection.TraversableLike",
        "scala.collection.GenTraversable",
        "scala.collection.TraversableOnce",
        "scala.collection.generic.GenericTraversableTemplate"),


      "scala.collection.SortedSetLike" -> List("scala.AnyRef",
        "scala.collection.generic.Sorted",
        "scala.collection.SetLike"),

      "scala.collection.generic.Sorted" -> List("scala.AnyRef"),

      "scala.collection.BitSetLike" -> List("scala.AnyRef",
        "scala.collection.SortedSetLike")
    )

    val buffer = new ListBuffer[String]

    def lin(clazz: String): Unit = {
      buffer += clazz
      for (c <- diagram(clazz).reverse) {
        lin(c)
      }
    }

    lin("scala.collection.BitSet")

    // remove duplicates from the right
    val set = new mutable.LinkedHashSet[String]
    set ++= buffer.reverse

    // remove AnyRef since its represented as java.lang.Object
    set.toList.reverse.filter(_ != "scala.AnyRef")
   }

  /**
   * Task 4:
   *
   * Provide a `CryptoLogger` trait that encrypts the log messages with the Caesar cipher.
   * The key should be 3 by default, but it should be overridable by the user.
   * Provide usage examples with the default key and a key of -3.
   */
  trait Logger {
    def log(msg: String): Unit
  }

  trait CryptoLogger extends Logger {
    val key: Int = 3

    abstract override def log(msg: String): Unit = {
      super.log(msg.map(c => (c + key).toChar))
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
  trait PropertyChangeSupportLike {
    private val support = new PropertyChangeSupport(this)

    def addPropertyChangeListener(listener: PropertyChangeListener): Unit =
      support.addPropertyChangeListener(listener)

    def removePropertyChangeListener(listener: PropertyChangeListener): Unit =
      support.removePropertyChangeListener(listener)

    def getPropertyChangeListeners: Array[PropertyChangeListener] =
      support.getPropertyChangeListeners

    def addPropertyChangeListener(propertyName: String, listener: PropertyChangeListener): Unit =
      support.addPropertyChangeListener(propertyName, listener)

    def removePropertyChangeListener(propertyName: String, listener: PropertyChangeListener): Unit =
      support.removePropertyChangeListener(propertyName, listener)

    def getPropertyChangeListeners(propertyName: String): Array[PropertyChangeListener] =
      support.getPropertyChangeListeners(propertyName)

    def firePropertyChange(propertyName: String, oldValue: Any, newValue: Any): Unit =
      support.firePropertyChange(propertyName, oldValue, newValue)

    def firePropertyChange(propertyName: String, oldValue: Int, newValue: Int): Unit =
      support.firePropertyChange(propertyName, oldValue, newValue)

    def firePropertyChange(propertyName: String, oldValue: Boolean, newValue: Boolean): Unit =
      support.firePropertyChange(propertyName, oldValue, newValue)

    def firePropertyChange(event: PropertyChangeEvent): Unit =
      support.firePropertyChange(event)

    def fireIndexedPropertyChange(propertyName: String, index: Int, oldValue: Any, newValue: Any): Unit =
      support.fireIndexedPropertyChange(propertyName, index, oldValue, newValue)

    def fireIndexedPropertyChange(propertyName: String, index: Int, oldValue: Int, newValue: Int): Unit =
      support.fireIndexedPropertyChange(propertyName, index, oldValue, newValue)

    def fireIndexedPropertyChange(propertyName: String, index: Int, oldValue: Boolean, newValue: Boolean): Unit =
      support.fireIndexedPropertyChange(propertyName, index, oldValue, newValue)

    def hasListeners(propertyName: String): Boolean =
      support.hasListeners(propertyName)
  }

  class PointBean(x: Int = 0, y: Int = 0) extends Point(x, y) with PropertyChangeSupportLike

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
   *
   * Solution:
   *
   * From the preferred design JContainer should extends from both Container and JComponent.
   * And this is not possible in Java since it supports only single inheritance.
   * But in Scala this design is possible through using traits.
   * In Scala JComponent could be a trait. JContainer could then extend Container and mixed with
   * JComponent trait, which is OK, since JComponent trait extends common Component class.
   */

  /**
   * Task 7:
   *
   * There are dozens of Scala trait tutorials with silly examples of barking dogs or
   * philosophizing frogs. Reading through contrived hierarchies can be tedious and not very
   * helpful, but designing your own is very illuminating. Make your own silly trait hierarchy
   * example that demonstrates layered traits, concrete and abstract methods, and concrete and
   * abstract fields.
   */
  trait Engine {
    val model: String
    def start(): Unit
    def stop(): Unit
  }

  trait InfiniteEngine extends Engine {
    val model = "infinite"

    def start(): Unit = {
      println("Starting infinite engine, model: " + model)
    }

    def stop(): Unit = {
      // infinite engine cannot be stopped
    }
  }

  class TestEngine extends Engine with InfiniteEngine {
    override val model = "test"

    override def start(): Unit = {
      println("Starting test engine, model: " + model)
      super.start()
    }

    override def stop(): Unit = {
      println("Stopping test engine")
      super.stop()
    }
  }

  /**
   * Task 8:
   *
   * In the `java.io` library, you add buffering to an input stream with a `BufferedInputStream`
   * decorator. Reimplement buffering as a trait. For simplicity, override the `read` method.
   */
  trait BufferedInputStreamLike extends InputStream with Logger {
    val bufferSize = 2048
    private val buffer = new Array[Byte](bufferSize)
    private var offset: Int = 0
    private var size: Int = 0

    abstract override def read(): Int = {
      if (size == -1) {
        return -1
      }

      if (offset >= size) {
        offset = 0
        size = 0

        log("Filling the buffer, bufferSize: " + bufferSize)
        fillBuffer(0)

        if (size == 0) {
          log("Reached stream end")
          size = -1
          return -1
        }

        log("Buffer is filled, size: " + size)
      }

      val byte = buffer(offset)
      offset += 1
      byte
    }

    @tailrec
    private def fillBuffer(index: Int): Unit = {
      if (index >= buffer.length) {
        return
      }

      val byte = super.read()
      if (byte == -1) {
        return
      }

      buffer(index) = byte.toByte
      size += 1
      fillBuffer(index + 1)
    }
  }

  /**
   * Task 9:
   *
   * Using the logger traits from this chapter, add logging to the solution of the preceding
   * problem that demonstrates buffering.
   */
  trait ConsoleLogger extends Logger {
    override def log(msg: String): Unit = println(msg)
  }

  /**
   * Task 10:
   *
   * Implement a class `IterableInputStream` that extends `java.io.InputStream` with the trait
   * `Iterable[Byte]`.
   */
  class IterableInputStream(private val in: InputStream) extends InputStream with Iterable[Byte] {
    if (in == null) {
      throw new NullPointerException("in")
    }

    override def close(): Unit = in.close()

    override def read(): Int = in.read()

    override def iterator: Iterator[Byte] = new Iterator[Byte] {
      private var nextByte: Int = 0

      override def hasNext: Boolean = {
        if (nextByte != -1) {
          nextByte = read()
        }

        nextByte != -1
      }

      override def next(): Byte = {
        nextByte.toByte
      }
    }
  }
}

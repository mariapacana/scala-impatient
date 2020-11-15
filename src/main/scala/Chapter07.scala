import scala.io.StdIn

/**
 * Task 1:
 *
 * <p>Write an example program to demonstrate that
 * <blockquote><code>
 *   package com.horstmann.impatient
 * </code></blockquote>
 * is not the same as
 * <blockquote><code>
 *   package com <br/>
 *   package horstmann <br/>
 *   package impatient <br/>
 * </code></blockquote>
 *
 * @see Chapter0701a.scala  for solution part A
 * @see Chapter0701b.scala  for solution part B
 */

/**
 * Task 2:
 *
 * <p>Write a puzzler that baffles your Scala friends, using a package <code>com</code>
 * that isn’t at the top level.
 */
package puzzler {
  package com {
    object Secret {
      val greeting = "hello"
    }
  }
}


/**
 * Task 3:
 *
 * <p>Write a package random with functions
 * nextInt(): Int,
 * nextDouble(): Double,
 * and setSeed(seed: Int): Unit.
 *
 * <p>To generate random numbers, use the linear congruential generator
 * next = previous × a + b mod 2n,
 * where a = 1664525, b = 1013904223, and n = 32.
 */
package object random {
  private var seed = 0
  private val a = 1664525
  private val b = 1013904223
  private val n = 32

  def nextInt(): Int = {
    seed = (seed*a + b) % Math.pow(2,n).toInt
    seed
  }

  def nextDouble(): Double = nextInt() / (Int.MaxValue.toDouble + 1.0)

  def setSeed(seed: Int): Unit = {
    this.seed = seed
  }
}

/**
 * Task 4:
 *
 * <p>Why do you think the Scala language designers provided the package object syntax instead
 * of simply letting you add functions and variables to a package?
 *
 * Because of JVM limitations?
 */

/**
 * Task 5:
 *
 * <p>What is the meaning of <code>private[com] def giveRaise(rate: Double)</code>?
 * Is it useful?
 *
 * It means that this function is visible outside its class to other classes within its package,
 * or its enclosing package.
 */


/**
 * Task 6:
 *
 * <p>Write a program that copies all elements from a Java hash map into a Scala hash map.
 * Use imports to rename both classes.
 */

/**
 * Task 7:
 *
 * <p>In the preceding exercise, move all imports into the innermost scope possible.
 */

package object JavaToScalaConverter {
  import java.util.{HashMap => JavaHashMap}
  import scala.collection.mutable.{HashMap => ScalaHashMap}
  def toHashMap[A,B](jHashMap: JavaHashMap[A,B]): ScalaHashMap[A,B] = {
    val emptyMap = ScalaHashMap.empty[A,B]
    jHashMap.forEach((k: A, v: B) => {
      emptyMap.put(k,v)
    })
    emptyMap
  }
}


/**
 * Task 8:
 *
 * <p>What is the effect of
 * <blockquote><code>
 *   import java._ <br/>
 *   import javax._ <br/>
 * </code></blockquote>
 * Is this a good idea?
 *
 * This imports EVERYTHING from standard Java and the Java extensions.
 * Seems likely that there will be name collisions between Scala & Java.
 */


/**
 * Task 9:
 *
 * <p>Write a program that imports the <code>java.lang.System</code> class,
 * reads the user name from the <code>user.name</code> system property,
 * reads a password from the <code>Console</code> object, and prints a message
 * to the standard error stream if the password is not "secret".
 * Otherwise, print a greeting to the standard output stream.
 * Do not use any other imports, and do not use any qualified names (with dots).
 */
object Chapter0709 extends App {
  import java.lang.System
  val username: String = System.getProperty("user.name")
  // You can't use the Console code object for reading values, only StdIn.
  val password: String = StdIn.readLine()
  if (password != "secret") {
    System.err.println("Wrong password!")
    System.exit(1)
  } else {
    System.out.println(s"Welcome ${username}")
    System.exit(0)
  }
}

/**
 * Task 10:
 *
 * <p>Apart from StringBuilder, what other members of java.lang does the scala package override?
 */


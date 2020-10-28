
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


/**
 * Task 4:
 *
 * <p>Why do you think the Scala language designers provided the package object syntax instead
 * of simply letting you add functions and variables to a package?
 */

/**
 * Task 5:
 *
 * <p>What is the meaning of <code>private[com] def giveRaise(rate: Double)</code>?
 * Is it useful?
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

/**
 * Task 8:
 *
 * <p>What is the effect of
 * <blockquote><code>
 *   import java._ <br/>
 *   import javax._ <br/>
 * </code></blockquote>
 * Is this a good idea?
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

/**
 * Task 10:
 *
 * <p>Apart from StringBuilder, what other members of java.lang does the scala package override?
 */


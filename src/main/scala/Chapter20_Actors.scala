import java.awt.Color
import java.awt.image.BufferedImage
import java.io.{File, IOException}
import java.util
import javax.imageio.ImageIO
import scala.collection.JavaConverters.mapAsScalaMapConverter
import scala.collection.immutable.Seq
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Promise}
import scala.io.Source
import scala.util.Random
import scala.util.matching.Regex

//noinspection ScalaDeprecation
object Chapter20_Actors {

  /**
   * Task 1:
   *
   * Write a program that generates an array of `n` random numbers (where `n` is a large value,
   * such as 1,000,000), and then computes the average of those numbers by distributing the work
   * over multiple actors, each of which computes the sum of the values, sending the result to
   * an actor that combines the results.
   * If you run your program on a dual-core or quad-core processor, what is the speedup over
   * a single-threaded solution?
   */

  /**
   * Task 2:
   *
   * Write a program that reads in a large image into a `BufferedImage`, using
   * `javax.imageio.ImageIO.read`. Use multiple actors, each of which inverts the colors in
   * a strip of the image. When all strips have been inverted, write the result.
   */

  /**
   * Task 3:
   *
   * Write a program that counts how many words match a given regular expression in all files of
   * all subdirectories of a given directory. Have one actor per file, one actor that traverses
   * the subdirectories, and one actor to accumulate the results.
   */

  /**
   * Task 4:
   *
   * Modify the program of the preceding exercise to display all matching words.
   */

  /**
   * Task 5:
   *
   * Modify the program of the preceding exercise to display all matching words, each with
   * a list of all files containing it.
   */

  /**
   * Task 6:
   *
   * Write a program that constructs 100 actors that use a `while(true)/receive` loop,
   * calling `println(Thread.currentThread)` when they receive a 'Hello message,
   * and 100 actors that do the same with `loop/react`. Start them all,
   * and send them all a message.
   * How many threads are occupied by the first kind, and how many by the second kind?
   */

  /**
   * Task 7:
   *
   * Add a supervisor to the program of exercise 3 that monitors the file reading actors
   * and logs any that exit with an `IOException`. Try triggering the exception by removing
   * files that have been scheduled for processing.
   */

  /**
   * Task 8:
   *
   * Show how an actor-based program can deadlock when one sends synchronous messages.
   */

  /**
   * Task 9:
   *
   * Produce a faulty implementation of the program in exercise 3, in which the actors update
   * a shared counter. Can you demonstrate that the program acts incorrectly?
   */

  /**
   * Task 10:
   *
   * Rewrite the program of exercise 1 by using channels for communication.
   */

}

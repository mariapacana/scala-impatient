import com.horstmann.impatient.{Chapter0701a, Chapter0701b}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class Chapter07Spec extends FlatSpec with Matchers {

    "Chapter0701b" should "not be able to access Chapter0701a" in {
      Chapter0701b.sayHello // Uses the greeting object from the same package
      Chapter0701a.sayHello // Uses a greeting object from an intermediate package
    }


  "puzzler" should "use a package com that isn’t at the top level" in {
    import puzzler._
    com.Secret.greeting shouldBe("hello")
  }

  "random" should "has nextInt, nextDouble, and setSeed functions" in {
      random.nextInt() shouldBe 1013904223
      random.nextDouble() shouldBe 0.557133817113936

      random.setSeed(1)
      random.nextInt() shouldBe 1015568748
    }

  "JavaToScalaConverter" should "copy all elements from a Java hash map into a Scala hash map" in {
    //given
    val javaHashMap = new java.util.HashMap[String, Int]
    javaHashMap.put("A", 1)
    javaHashMap.put("B", 2)
    javaHashMap.put("C", 3)

    //when
    val scalaHashMap: mutable.HashMap[String, Int] = JavaToScalaConverter.toHashMap(javaHashMap)

    //then
    scalaHashMap.size shouldBe javaHashMap.size
    scalaHashMap("A") shouldBe 1
    scalaHashMap("B") shouldBe 2
    scalaHashMap("C") shouldBe 3
  }

//  "Chapter0709" should "print to error stream if password is not secret" in {
//    //given
//    val password = "wrong"
//
//    //when
//    val (exit, _, err) = TestUtils.runAppWithInput(password, "Chapter0709")
//
//    //then
//    exit shouldBe 1
//    err.contains("Wrong password!") shouldBe true
//  }
//
//  it should "print greeting to standard output if password is secret" in {
//    //given
//    val password = "secret"
//
//    //when
//    val (exit, out, err) = TestUtils.runAppWithInput(password, "Chapter0709")
//
//    //then
//    exit shouldBe 0
//    out.contains("Welcome " + System.getProperty("user.name")) shouldBe true
//    err shouldBe ""
//  }
}

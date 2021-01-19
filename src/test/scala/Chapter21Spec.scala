import Chapter21.{Fraction, RichFraction, average, smaller}
import TestUtils.withOutputAndResult
import java.awt.{Point => jPoint}
import java.io.StringReader

import org.scalatest.{FlatSpec, Matchers}

import scala.language.postfixOps
import scala.math.abs

class Chapter21Spec extends FlatSpec with Matchers {

  "PercentAdder" should "define an operator `+%` and use implicit conversion" in {
    import Chapter21._

    120 +% 10 shouldBe 132
  }

  "Int2Factorial" should "define an operator `!` and use implicit conversion" in {
    import Chapter21._

    (5!) shouldBe 120
  }

//  "FluentReader" should "provide fluent APIs for reading data from the console" in {
//    //given
//    import Chapter21._
//
//    val input = """Viktor
//                  |35
//                  |80
//                  |""".stripMargin
//
//    //when
//    val (out: String, result: FluentReader) = withOutputAndResult {
//      Console.withIn(new StringReader(input)) {
//        Read in aString askingFor "Your name" and
//          anInt askingFor "Your age" and
//          aDouble askingFor "Your weight"
//      }
//    }
//
//    //then
//    result.getData.mkString("\n") shouldBe """(Your name,Viktor)
//                                             |(Your age,35)
//                                             |(Your weight,80.0)""".stripMargin
//
//    out shouldBe """Your name: Your age: Your weight: """.stripMargin
//  }
//
  "smaller" should "work with Fraction instances" in {

    //when & then
    smaller(Fraction(1, 7), Fraction(2, 9)) shouldEqual Fraction(1, 7)
    smaller(Fraction(1, 7), Fraction(1, 7)) == Fraction(1, 7)
    smaller(Fraction(0, 7), Fraction(0, 9)) == Fraction(0, 9)
  }

  "LexicographicPointOrdering" should "compare Point objects by lexicographic comparison" in {
    //given
    import Chapter21.LexicographicPointOrdering

    //when & then
    new jPoint(1, 1) == new jPoint(1, 1)
    new jPoint(1, 1) should be < new jPoint(2, 1)
    new jPoint(1, 1) should be < new jPoint(1, 2)
    new jPoint(1, -3) should be < new jPoint(1, 2)
  }

  "DistancePointOrdering" should "compare Point objects by distance to the origin" in {
    //given
    import Chapter21.DistancePointOrdering

    //when & then
    new jPoint(1, 1) == (new jPoint(1, 1))
    new jPoint(-2, 1) == (new jPoint(2, 1))
    new jPoint(1, 1) should be < new jPoint(1, 2)
    new jPoint(1, -3) should be > new jPoint(1, 2)
  }

  "average" should "calculate the average of a sequence of values" in {
    import Chapter21.{NumberLike, average}

    average(5.0, 2.5, 7.5) shouldEqual 5.0
    average(BigDecimal(5.0), BigDecimal(2.5), BigDecimal(7.5)) shouldEqual BigDecimal(5.0)
  }

  "divBy for a NumberLikeString" should "retain every nth letter" in {
    import Chapter21.{NumberLike, average}
    average("Hello","World") shouldEqual "Hlool"
  }
}

import java.util

import Chapter04._
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable.{Map => mMap, SortedMap => mSortedMap, LinkedHashMap => mLinkedHashMap}

class Chapter04Spec extends FlatSpec with Matchers {

  "Gizmos with reduced price" should "reduce the value of the gizmos by 90%" in {
    val gizmos = Map("Milk Frother" -> 50.0, "Pack and Play" -> 100.0, "Projector" -> 300.0)
    val reducedGizmos: Map[String, Double] = gizmosWithReducedPrice(gizmos)
    reducedGizmos("Milk Frother") shouldBe 45.0
    reducedGizmos("Pack and Play") shouldBe 90.0
    reducedGizmos("Projector") shouldBe 270.0
  }

  it should "count words using mutable Map" in {
    val words: mMap[String, Int] = countWordsMutableMap()

    assertWordsMap(words)
  }

  it should "count words using immutable Map" in {
    val words: Map[String, Int] = countWordsImmutableMap()

    assertWordsMap(words)
  }

  it should "count words using SortedMap" in {
    val words: mSortedMap[String, Int] = countWordsSortedMap()

    assertWordsOrderedMap(words.toList)
  }

  it should "count words using TreeMap" in {
    val words: mMap[String, Int] = countWordsTreeMap()

    assertWordsOrderedMap(words.toList)
  }

  it should "visit weekdays in insertion order" in {
    val weekdays: mLinkedHashMap[String, Int] = weekdaysLinkedHashMap()

    weekdays.size shouldBe 7
    val it = weekdays.keysIterator
    it.next() shouldBe "Monday"
    it.next() shouldBe "Tuesday"
  }

//  it should "format Java properties" in {
//    val props: List[String] = formatJavaProperties()
//    props.size should be > 0
//
//    val separatorIndex = props.head.indexOf('|')
//    separatorIndex should be > 0
//    for (prop <- props) {
//      prop.indexOf('|') shouldBe separatorIndex
//    }
//  }
//
  it should "return min and max values from Array[Int]" in {
    val (min: Int, max: Int) = minmax(Array(1, -2, 3, 0, 5, 4))

    min shouldBe -2
    max shouldBe 5
  }

  it should "count less than, equal to, and greater than in Array[Int]" in {
    val (lt: Int, eq: Int, gt: Int) = lteqgt(Array(1, -2, 3, 0, 5, 4), 1)

    lt shouldBe 2
    eq shouldBe 1
    gt shouldBe 3
  }

  private def assertWordsMap(words: collection.Map[String, Int]): Unit = {
    words.size shouldBe 12
    words("Simple") shouldBe 1
    words("text") shouldBe 1
    words("file") shouldBe 2
    words("with") shouldBe 1
    words("example") shouldBe 1
    words("words.") shouldBe 2
    words("We") shouldBe 1
    words("will") shouldBe 1
    words("parse") shouldBe 1
    words("the") shouldBe 2
    words("and") shouldBe 1
    words("count") shouldBe 1
  }

  private def assertWordsOrderedMap(mapAsList: List[(String, Int)]): Unit = {
    mapAsList.size shouldBe 12
    mapAsList shouldBe List(
      "Simple" -> 1,
      "We" -> 1,
      "and" -> 1,
      "count" -> 1,
      "example" -> 1,
      "file" -> 2,
      "parse" -> 1,
      "text" -> 1,
      "the" -> 2,
      "will" -> 1,
      "with" -> 1,
      "words." -> 2)
  }
}

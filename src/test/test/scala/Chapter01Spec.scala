import Chapter01._
import org.scalatest.{FlatSpec, Matchers}

class Chapter01Spec extends FlatSpec with Matchers {

  "computeBigInt" should "compute 2^1024" in {
    //when & then
    computeBigInt() shouldBe BigInt("1797693134862315907729305190789024733617976978942306572734" +
      "300811577326758055009631327084773224075360211201138798713933576587897688144166224928474" +
      "3063947412437776789342486548527630221960124609411945308295208500576883815068234246288147" +
      "3913110540827237163350510684586298239947245938479716304835356329624224137216")
  }

  "randomFileName" should "yield a string such as 'qsnvbevtomcj38o06kul'" in {
    //when
    val result: String = randomFileName()

    //then
    result.length shouldBe 20
    result.matches("[a-z0-9]+") shouldBe true
  }
}

import model.NGramsAnalyser
import org.scalatest._

class NgramTests extends FunSuite {

  test("testNgramCount") {
    val nGramsAnalyser = new NGramsAnalyser
    val testData = Vector("tHe ApPlE fElL fRoM tHe TrEe")
    val expectedResult: Map[String, Int] = Map(
      "th" -> 2, "he" -> 2, "ap" -> 1, "pp" -> 1, "pl" -> 1, "le" -> 1, "fe" -> 1, "el" -> 1, "ll" -> 1, "fr" -> 1, "ro" -> 1, "om" -> 1, "tr" -> 1, "re" -> 1, "ee" -> 1,
    )
    val result = nGramsAnalyser getNgrams(testData, 2)
    assert(result equals expectedResult)
  }

  test("testSkipGramCount") {
    val nGramsAnalyser = new NGramsAnalyser
    val testData = Vector("tHe ApPlE fElL fRoM tHe TrEe")
    val expectedResult: Map[String, Int] = Map(
      "t_e" -> 3, "a_p" -> 1, "p_l" -> 1, "p_e" -> 1, "f_l" -> 1, "e_l" -> 1, "f_o" -> 1, "r_m" -> 1, "r_e" -> 1
    )
    val result = nGramsAnalyser.getSkipGrams(testData)
    assert(result equals expectedResult)
  }
}

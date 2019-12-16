import model.NGramsAnalyser
import org.scalatest._

class NgramTests extends FunSuite {

  test("testNgramCount") {
    val testData = Vector(
      "the apple fell from the tree",
      "another sentence to test and play with",
      "i need some inspiration for this test",
      "please work"
    )
    val expectedResult: Map[String, Int] = Map(
      "th" -> 5, "he" -> 3, "te" -> 3, "pl" -> 3, "or" -> 2, "ea" -> 2,
      "se" -> 2, "le" -> 2, "st" -> 2, "om" -> 2, "en" -> 2, "es" -> 2,
      "hi" -> 2, "an" -> 2, "ee" -> 2, "is" -> 1, "fe" -> 1, "ns" -> 1,
      "la" -> 1, "to" -> 1, "pp" -> 1, "ir" -> 1, "nc" -> 1, "wi" -> 1, "in" -> 1
    )
    val result = NGramsAnalyser getNgrams(testData, 2, 25)
    assert(result == expectedResult)
  }

  test("testSkipGramCount") {
    val testData = Vector(
      "the apple fell from the tree",
      "another sentence to test and play with",
      "i need some inspiration for this test",
      "please work"
    )
    val expectedResult: Map[String, Int] = Map(
      "t_e" -> 4, "n_e" -> 3, "e_t" -> 3, "p_e" -> 2, "t_i" -> 2,
      "t_s" -> 2, "r_t" -> 1, "s_p" -> 1, "f_l" -> 1, "r_m" -> 1,
      "a_e" -> 1, "a_o" -> 1, "r_e" -> 1, "r_e" -> 1, "e_n" -> 1,
      "e_s" -> 1, "a_i" -> 1, "i_h" -> 1, "w_r" -> 1, "h_s" -> 1,
      "e_d" -> 1, "a_d" -> 1, "s_m" -> 1, "n_t" -> 1, "e_c" -> 1, "t_n" -> 1
    )
    val result = NGramsAnalyser.getSkipGrams(testData,25)
    assert(result == expectedResult)
  }

  test("getNgramFrequency") {
    val testData = Vector(
      "the apple fell from the tree",
      "another sentence to test and play with",
      "i need some inspiration for this test",
      "please work some more words"
    )
    val result1 = NGramsAnalyser.getNgramCount(testData, "th")
    // 5/25
    assert(result1 == 0.2)
    val result2 = NGramsAnalyser.getNgramCount(testData, "in")
    assert(result2 == 0.04)
  }
}

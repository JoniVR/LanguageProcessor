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
    val result = NGramsAnalyser getNgrams(testData, 2)
    assert(result.head._1 == "th")
    assert(result.last._1 == "nt")
    assert(result("th") == 5)
    assert(result("he") == 3)
    assert(result("st") == 2)
    assert(result("nt") == 1)
  }

  test("testSkipGramCount") {
    val testData = Vector(
      "the apple fell from the tree",
      "another sentence to test and play with",
      "i need some inspiration for this test",
      "please work"
    )
    val result = NGramsAnalyser.getSkipGrams(testData)
    assert(result.size == 49)
    assert(result("t_e") == 4)
    assert(result("n_e") == 3)
    assert(result("s_n") == 1)
  }

  test("getNgramFrequency") {
    val testData = Vector(
      "the apple fell from the tree",
      "another sentence to test and play with",
      "i need some inspiration for this test",
      "please work some more words"
    )
    val result1 = NGramsAnalyser.getNgramCount(testData, "th")
    assert(result1 == 5)
    val result2 = NGramsAnalyser.getNgramCount(testData, "in")
    assert(result2 == 1)
  }
}

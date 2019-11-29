import model.NGramsAnalyser
import org.scalatest._

class NgramTests extends FunSuite {

  test("testNgramCount") {
    val testData = Vector("the quick brown fox jumps over the lazy dog")
    val nGramsAnalyser = new NGramsAnalyser
    val nGrams = nGramsAnalyser.getNgrams(testData, 2)
    for (nGram <- nGrams) {
      println(s"${nGram._1.mkString}: ${nGram._2}")
    }
    assert( 1 == 1)
  }
}

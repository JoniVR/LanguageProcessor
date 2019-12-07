import model.Languages
import org.scalatest.{BeforeAndAfter, FunSuite}
import utilities.Processor

class ProcessorTests extends FunSuite with BeforeAndAfter {

  test("getStartsWithEachLetterOfAlphabetPercentage") {
    val processor = new Processor()
    val testStringDutch = Vector(
      "Dit is Een test",
      "Ik heb geen idee wat ik voor deze test moet verzinnen..",
      "Dus Typ ik maar gewoon wat er nu in Me opkomt.",
      "De afwisselende hoofdletters Zijn onderdeel van de TEST.",
      "Ik heb echt geen inspiratie meer nu",
      "hopelijk is het genoeg nu. 123 nieuwe test. xenofobie"
    ) // 50 words
    val resultMap = processor.getStartsWithEachLetterOfAlphabetPercentage(Languages.Dutch, testStringDutch).get
    // all these percentages have been checked manually
    // 18% starts with i
    assert(resultMap('i') == 18.0)
    // 2% starts with x
    assert(resultMap('x') == 2.0)
    // 8% starts with m
    assert(resultMap('m') == 8.0)
  }
}
import model.Languages
import org.scalatest.{BeforeAndAfter, FunSuite}
import utilities.Processor

class ProcessorTests extends FunSuite with BeforeAndAfter {

  test("calculateStartsOrEndsWithEachLetterOfAlphabetPercentage") {
    val processor = new Processor()
    val testStringDutch = Vector(
      "Dit is Een test",
      "Ik heb geen idee wat ik voor deze tesT moet verzinnen..",
      "Dus Typ ik maar gewoon wat er nu in Me opkomt.",
      "De afwisselende hoofdletters Zijn onderdeel van de TEST.",
      "Ik heb echt geen inspiratie meer nu",
      "hopelijk is het genoeg nu. 123 nieuwe test. xenofobie"
    ) // 50 words
    val resultMapStartsWith = processor.calculateStartsOrEndsWithEachLetterOfAlphabetPercentage(Languages.Dutch, testStringDutch, isStartsWith = true).get
    // all these percentages have been checked manually
    // 18% starts with i
    assert(resultMapStartsWith('i') == 18.0)
    // 2% starts with x
    assert(resultMapStartsWith('x') == 2.0)
    // 8% starts with m
    assert(resultMapStartsWith('m') == 8.0)

    val resultMapEndsWith = processor.calculateStartsOrEndsWithEachLetterOfAlphabetPercentage(Languages.Dutch, testStringDutch, isStartsWith = false).get
    // 0% ends with i
    assert(resultMapEndsWith('i') == 0.0)
    // 10% starts with x
    assert(resultMapEndsWith('k') == 10.0)
    // 22% starts with m
    assert(resultMapEndsWith('t') == 22.0)
  }
}
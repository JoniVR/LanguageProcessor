import model.Languages
import org.scalatest.{BeforeAndAfter, FunSuite}
import utilities.Processor

import scala.math.ScalaNumber

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
    val resultMapStartsWith = processor.calculateStartsOrEndsWithEachLetterOfAlphabetPercentage(Languages.Dutch, testStringDutch, isStartsWith = true)
    // all these percentages have been checked manually
    // 18% starts with i
    assert(resultMapStartsWith('i') == 18.0)
    // 2% starts with x
    assert(resultMapStartsWith('x') == 2.0)
    // 8% starts with m
    assert(resultMapStartsWith('m') == 8.0)

    val resultMapEndsWith = processor.calculateStartsOrEndsWithEachLetterOfAlphabetPercentage(Languages.Dutch, testStringDutch, isStartsWith = false)
    // 0% ends with i
    assert(resultMapEndsWith('i') == 0.0)
    // 10% starts with x
    assert(resultMapEndsWith('k') == 10.0)
    // 22% starts with m
    assert(resultMapEndsWith('t') == 22.0)

    val testEmptyString = Vector("")

    val resultEmptyString = processor.calculateStartsOrEndsWithEachLetterOfAlphabetPercentage(Languages.Spanish, testEmptyString, isStartsWith = true)
    assert(resultEmptyString('a') == 0)
  }

  test("calculateAlphabetLetterFrequencyPercentage") {
    val processor = new Processor()
    val testStringDutch = Vector(
      "Dit is Een test",
      "Ik heb een idee wat ik voor deze tesT moet verzinnen..",
      "Dus Typ ik maar gewoon wat er nu in Me opkomt.",
      "De afwisselende hoofdletters Zijn onderdeel van de TEST.",
      "Ik heb echt geen inspiratie meer nu",
      "hopelijk is het genoeg nu. 123 test. xenofobie äö"
    ) // 200 characters (zonder deense characters, spaties en leestekens, enkel de letters binnen nl alfabet geteld dus)
    val resultMap = processor.calculateAlphabetLetterFrequencyPercentage(Languages.Dutch, testStringDutch)
    // 17/200, 8.5% of the text should be the letter 'i'
    assert(resultMap('i') == 8.5)
    // 10/200, 5% of the text should be the letter 'd'
    assert(resultMap('d') == 5)
    // ä should not be counted and thus not be present
    assert(resultMap.get('ä') == Option.empty)
  }
}
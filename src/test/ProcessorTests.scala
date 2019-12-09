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
    assert(resultMapStartsWith('i') == 0.18)
    // 2% starts with x
    assert(resultMapStartsWith('x') == 0.02)
    // 8% starts with m
    assert(resultMapStartsWith('m') == 0.08)

    val resultMapEndsWith = processor.calculateStartsOrEndsWithEachLetterOfAlphabetPercentage(Languages.Dutch, testStringDutch, isStartsWith = false)
    // 0% ends with i
    assert(resultMapEndsWith('i') == 0.0)
    // 10% starts with x
    assert(resultMapEndsWith('k') == 0.10)
    // 22% starts with m
    assert(resultMapEndsWith('t') == 0.22)

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
    assert(resultMap('i') == 0.085)
    // 10/200, 5% of the text should be the letter 'd'
    assert(resultMap('d') == 0.05)
    // ä should not be counted and thus not be present
    assert(resultMap.get('ä') == Option.empty)
  }

  test("calculateVowelsAndConsonantsFrequencyPercentage"){
    val testStringDutch = Vector(
      "Dit is Een test",
      "Ik heb een idee wat ik voor deze tesT moet verzinnen..",
      "Dus Typ ik maar gewoon wat er nu in Me opkomt.",
      "De afwisselende hoofdletters Zijn onderdeel van de TEST.",
      "Ik heb echt geen inspiratie meer nu",
      "hopelijk is het genoeg nu. 123 test. xenofobie äö"
    ) // 200 characters (zonder deense characters, spaties en leestekens, enkel de letters binnen nl alfabet geteld dus)
    val processor = new Processor()
    val resultMap = processor.calculateVowelsAndConsonantsFrequencyPercentage(Languages.Dutch, testStringDutch)
    // 84/200 - vowels -> 0,42
    assert(resultMap("vowels") == 0.42)
    // 116/200 - consonants -> 0,58
    assert(resultMap("consonants") == 0.58)
  }
}
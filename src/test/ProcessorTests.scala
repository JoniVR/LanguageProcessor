import model.Languages
import org.scalatest.{BeforeAndAfter, FunSuite}
import utilities.Processor

import scala.math.ScalaNumber

class ProcessorTests extends FunSuite with BeforeAndAfter {

  test("filterNonAlphabetCharacters") {
    val testStringDutch = Vector(
      "Dit is Een test",
      "Ik heb een idee wat ik voor deze tesT moet verzinnen..",
      "Dus Typ ik maar gewoon wat er nu in Me opkomt.",
      "De afwisselende hoofdletters Zijn onderdeel van de TEST.",
      "Ik heb echt geen inspiratie meer nu",
      "hopelijk is het genoeg nu. 123 test. xenofobie äö"
    )

    val expectedStringDutch = Vector(
      "dit is een test",
      "ik heb een idee wat ik voor deze test moet verzinnen",
      "dus typ ik maar gewoon wat er nu in me opkomt",
      "de afwisselende hoofdletters zijn onderdeel van de test",
      "ik heb echt geen inspiratie meer nu",
      "hopelijk is het genoeg nu  test xenofobie "
    )

    val result = Processor.filterNonAlphabetCharacters(testStringDutch, Languages.Dutch)
    assert(result == expectedStringDutch)
  }

  test("calculateStartsOrEndsWithEachLetterOfAlphabetPercentage") {
    val testStringDutch = Vector(
      "dit is een test",
      "ik heb geen idee wat ik voor deze test moet verzinnen",
      "dus typ ik maar gewoon wat er nu in me opkomt",
      "de afwisselende hoofdletters zijn onderdeel van de test",
      "ik heb echt geen inspiratie meer nu",
      "hopelijk is het genoeg nu nieuwe test xenofobie hello"
    ) // 50 words
    val resultMapStartsWith = Processor.calculateStartsOrEndsWithEachLetterOfAlphabetPercentage(Languages.Dutch, testStringDutch, isStartsWith = true)
    // all these percentages have been checked manually
    // 18% starts with i
    assert(resultMapStartsWith('i') == 0.18)
    // 2% starts with x
    assert(resultMapStartsWith('x') == 0.02)
    // 8% starts with m
    assert(resultMapStartsWith('m') == 0.08)

    val resultMapEndsWith = Processor.calculateStartsOrEndsWithEachLetterOfAlphabetPercentage(Languages.Dutch, testStringDutch, isStartsWith = false)
    // 0% ends with i
    assert(resultMapEndsWith('i') == 0.0)
    // 10% starts with x
    assert(resultMapEndsWith('k') == 0.10)
    // 22% starts with m
    assert(resultMapEndsWith('t') == 0.22)

    val testEmptyString = Vector("")

    val resultEmptyString = Processor.calculateStartsOrEndsWithEachLetterOfAlphabetPercentage(Languages.Dutch, testEmptyString, isStartsWith = true)
    assert(resultEmptyString('a') == 0)
  }

  test("calculateAlphabetLetterFrequencyPercentage") {
    val testStringDutch = Vector(
      "dit is een test",
      "nog een lijn om te testen ertussen test test",
      "ik heb geen idee wat ik voor deze test moet verzinnen",
      "dus typ ik maar gewoon wat er nu in me opkomt",
      "de afwisselende hoofdletters zijn onderdeel van de test",
      "ik heb echter geen inspiratie meer nu",
      "hopelijk is het genoeg nu nieuwe test xenofobie hello"
    ) // 250 characters
    val resultMap = Processor.calculateAlphabetLetterFrequencyPercentage(Languages.Dutch, testStringDutch)
    // 19/250, 7.6% of the text should be the letter 'i'
    assert(resultMap('i') == 0.076)
    // 10/200, 4% of the text should be the letter 'd'
    assert(resultMap('d') == 0.04)
}

  test("calculateVowelsAndConsonantsFrequencyPercentage") {
    val testStringDutch = Vector(
      "dit is een test",
      "nog een lijn om te testen ertussen test test",
      "ik heb geen idee wat ik voor deze test moet verzinnen",
      "dus typ ik maar gewoon wat er nu in me opkomt",
      "de afwisselende hoofdletters zijn onderdeel van de test",
      "ik heb echter geen inspiratie meer nu",
      "hopelijk is het genoeg nu nieuwe test xenofobie hello"
    ) // 250 characters
    val resultMap = Processor.calculateVowelsAndConsonantsFrequencyPercentage(Languages.Dutch, testStringDutch)
    // 104/250 - vowels -> 0,416
    assert(resultMap("vowels") == 0.416)
    // 146/250 - consonants -> 0,584
    assert(resultMap("consonants") == 0.584)
  }
}
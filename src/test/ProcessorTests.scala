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
    assert(result equals expectedStringDutch)
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

  test("calculateNumberOfWordsStartingOrEndingWithTopTwentyFiveBigrams") {

    val testStringDutch = Vector(
      "dit is een test",
      "nog een lijn om te testen ertussen test test",
      "ik heb geen idee wat ik voor deze test moet verzinnen",
      "dus typ ik maar gewoon wat er nu in me opkomt",
      "de afwisselende hoofdletters zijn onderdeel van de test",
      "ik heb echter geen inspiratie meer nu",
      "hopelijk is het genoeg nu nieuwe test xenofobie hello"
    ) // 250 characters

    val resultStartsWith = Processor.calculateNumberOfWordsStartingOrEndingWithTopTwentyFiveBigrams(testStringDutch, isStartsWith = true)
    val resultMapStartsWith = Map(
      "se" -> 0, "st" -> 0, "in" -> 2, "is" -> 2, "nu" -> 3,
      "el" -> 0, "ge" -> 4, "at" -> 0, "wa" -> 2, "es" -> 0,
      "oo" -> 0, "op" -> 1, "ij" -> 0, "nd" -> 0, "et" -> 0,
      "de" -> 3, "en" -> 0, "he" -> 4, "te" -> 8, "ie" -> 0,
      "ee" -> 2, "no" -> 1, "ik" -> 4, "ti" -> 0, "er" -> 2
    )
    assert(resultStartsWith equals resultMapStartsWith)

    val resultEndsWith = Processor.calculateNumberOfWordsStartingOrEndingWithTopTwentyFiveBigrams(testStringDutch, isStartsWith = false)
    val resultMapEndsWith = Map(
      "se" -> 0, "st" -> 6, "in" -> 1, "is" -> 2, "nu" -> 3,
      "el" -> 1, "ge" -> 0, "at" -> 2, "wa" -> 0, "es" -> 0,
      "oo" -> 0, "op" -> 0, "ij" -> 0, "nd" -> 0, "et" -> 2,
      "de" -> 3, "en" -> 7, "he" -> 0, "te" -> 1, "ie" -> 2,
      "ee" -> 1, "no" -> 0, "ik" -> 4, "ti" -> 0, "er" -> 3
    )
    assert(resultEndsWith equals resultMapEndsWith)
  }
}
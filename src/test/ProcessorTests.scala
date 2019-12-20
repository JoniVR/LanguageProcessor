import model.{Languages, Processor}
import org.scalatest.{BeforeAndAfter, FunSuite}

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
    assert(resultMapStartsWith("i") == 0.18)
    // 2% starts with x
    assert(resultMapStartsWith("x") == 0.02)
    // 8% starts with m
    assert(resultMapStartsWith("m") == 0.08)

    val resultMapEndsWith = Processor.calculateStartsOrEndsWithEachLetterOfAlphabetPercentage(Languages.Dutch, testStringDutch, isStartsWith = false)
    // 0% ends with i
    assert(resultMapEndsWith("i") == 0.0)
    // 10% starts with x
    assert(resultMapEndsWith("k") == 0.10)
    // 22% starts with m
    assert(resultMapEndsWith("t") == 0.22)

    val testEmptyString = Vector("")

    val resultEmptyString = Processor.calculateStartsOrEndsWithEachLetterOfAlphabetPercentage(Languages.Dutch, testEmptyString, isStartsWith = true)
    assert(resultEmptyString("a") == 0)
  }

  test("calculateAlphabetLetterPercentage") {
    val testStringDutch = Vector(
      "dit is een test",
      "nog een lijn om te testen ertussen test test",
      "ik heb geen idee wat ik voor deze test moet verzinnen",
      "dus typ ik maar gewoon wat er nu in me opkomt",
      "de afwisselende hoofdletters zijn onderdeel van de test",
      "ik heb echter geen inspiratie meer nu",
      "hopelijk is het genoeg nu nieuwe test xenofobie hello"
    ) // 250 characters
    val resultMap = Processor.calculateAlphabetLetterPercentage(Languages.Dutch, testStringDutch)
    // 19/250, 7.6% of the text should be the letter "i"
    assert(resultMap("i") == 0.076)
    // 10/200, 4% of the text should be the letter "d"
    assert(resultMap("d") == 0.04)
}

  test("calculateVowelsAndConsonantsPercentage") {
    val testStringDutch = Vector(
      "dit is een test",
      "nog een lijn om te testen ertussen test test",
      "ik heb geen idee wat ik voor deze test moet verzinnen",
      "dus typ ik maar gewoon wat er nu in me opkomt",
      "de afwisselende hoofdletters zijn onderdeel van de test",
      "ik heb echter geen inspiratie meer nu",
      "hopelijk is het genoeg nu nieuwe test xenofobie hello"
    ) // 250 characters
    val resultMap = Processor.calculateVowelsAndConsonantsPercentage(Languages.Dutch, testStringDutch)
    // 104/250 - vowels -> 0,416
    assert(resultMap("vowels") == 0.416)
    // 146/250 - consonants -> 0,584
    assert(resultMap("consonants") == 0.584)
  }

  test("calculateNumberOfWordsStartingOrEndingWithTop25Bigrams") {
    val testStringDutch = Vector(
      "dit is een test",
      "nog een lijn om te testen ertussen test test",
      "ik heb geen idee wat ik voor deze test moet verzinnen",
      "dus typ ik maar gewoon wat er nu in me opkomt",
      "de afwisselende hoofdletters zijn onderdeel van de test",
      "ik heb echter geen inspiratie meer nu",
      "hopelijk is het genoeg nu nieuwe test xenofobie hello"
    ) // 250 characters

    val resultStartsWith = Processor.calculateNumberOfWordsStartingOrEndingWithTop25Bigrams(testStringDutch, isStartsWith = true)
    assert(resultStartsWith("te") == 8)
    assert(resultStartsWith("wa") == 2)
    assert(resultStartsWith("ik") == 4)
    assert(resultStartsWith.size == 25)

    val resultEndsWith = Processor.calculateNumberOfWordsStartingOrEndingWithTop25Bigrams(testStringDutch, isStartsWith = false)
    assert(resultEndsWith("te") == 1)
    assert(resultEndsWith("wa") == 0)
    assert(resultEndsWith("ik") == 4)
    assert(resultEndsWith.size == 25)
  }

  test("calculateTop25BigramAndTrigramPercentage") {
    val testStringDutch = Vector(
      "dit is een test",
      "nog een lijn om te testen ertussen test test",
      "ik heb geen idee wat ik voor deze test moet verzinnen",
      "dus typ ik maar gewoon wat er nu in me opkomt",
      "de afwisselende hoofdletters zijn onderdeel van de test",
      "ik heb echter geen inspiratie meer nu",
      "hopelijk is het genoeg nu nieuwe test xenofobie hello"
    ) // 250 characters, 59 words, bigramCount = 197, trigramCount = 144
    val resultTuple = Processor.calculateTop25BigramAndTrigramPercentage(testStringDutch)
    val bigramsResultMap = resultTuple._1
    val trigramsResultMap = resultTuple._2

    // "te" is present 11 times in 197 bigrams
    assert(bigramsResultMap("te") == 0.05583756345177665)
    // "ge" is present 4 times in 197 bigrams
    assert(bigramsResultMap("ge") == 0.02030456852791878)
    assert(bigramsResultMap.size == 25)
    // "est" is present 7 times in 144 trigrams
    assert(trigramsResultMap("est") == 0.04861111111111111)
    // "ter" is present 2 times in 144 trigrams
    assert(trigramsResultMap("ter") == 0.013888888888888888)
    assert(trigramsResultMap.size == 25)
  }

  test("calculateTop25SkipgramPercentage") {
    val testStringDutch = Vector(
      "dit is een test",
      "nog een lijn om te testen ertussen test test",
      "ik heb geen idee wat ik voor deze test moet verzinnen",
      "dus typ ik maar gewoon wat er nu in me opkomt",
      "de afwisselende hoofdletters zijn onderdeel van de test",
      "ik heb echter geen inspiratie meer nu",
      "hopelijk is het genoeg nu nieuwe test xenofobie hello"
    ) // 250 characters, 59 words, skipgramCount = 95
    val resultMap = Processor.calculateTop25SkipgramPercentage(testStringDutch)
    // "e_t" is present 9 times in 95 skipgrams
    assert(resultMap("e_t") == 0.0625)
    // "s_e" is present 3 times in 95 skipgrams
    assert(resultMap("s_e") == 0.020833333333333332)
    assert(resultMap.size == 25)
  }

  test("calculateBigramAndSkipgramMatchingPercentage") {
    val testStringDutch = Vector(
      "dit is een test",
      "nog een lijn om te testen ertussen test test",
      "ik heb geen idee wat ik voor deze test moet verzinnen",
      "dus typ ik maar gewoon wat er nu in me opkomt",
      "de afwisselende hoofdletters zijn onderdeel van de test",
      "ik heb echter geen inspiratie meer nu",
      "hopelijk is het genoeg nu nieuwe test xenofobie hello"
    ) // 250 characters, 59 words, bigramCount = 197, skipgramCount = 95

    val resultTuple = Processor.calculateBigramAndSkipgramMatchingPercentage(testStringDutch)
    val skipGramMap = resultTuple._1
    val biGramMap = resultTuple._2
    // test if a bigram is present for every skipgram
    assert(skipGramMap.map(v => (v._1.replaceAll("_",""),v._2)).keys == biGramMap.keys)
    // test if both maps are 25 elements in size
    assert(skipGramMap.size == 25)
    assert(biGramMap.size == 25)
    assert(skipGramMap("t_s") == 0.05555555555555555)
    assert(biGramMap("ts") == 0)
    assert(skipGramMap("s_i") == 0.020833333333333332)
    assert(biGramMap("si") == 0)
    assert(skipGramMap("e_t") == 0.0625)
    assert(biGramMap("et") == 0.015228426395939087)
  }
}
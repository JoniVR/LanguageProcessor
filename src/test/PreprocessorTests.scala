package test

import org.scalatest._
import utilities.Preprocessor

class PreprocessorTests extends FunSuite {

  test("testRemoveSpaces"){
    val preprocessor = new Preprocessor()
    val stringToTest = "( De notulen van preventie- en crisisbeheerscapaciteiten worden goedgekeurd ) Mijnheer de Voorzitter , ik heb een opmerking over de agenda van vandaag ."
    val expectedResult = "(De notulen van preventie-en crisisbeheerscapaciteiten worden goedgekeurd) Mijnheer de Voorzitter, ik heb een opmerking over de agenda van vandaag."
    val result = preprocessor.removeSpaces(stringToTest)
    assert(result === expectedResult)
  }

  test("findSpaceLines") {
    val preprocessor = new Preprocessor()
    val emptyLine = " "
    assert(preprocessor.findSpaceLines(emptyLine))
    val emptyLine2 = ""
    assert(preprocessor.findSpaceLines(emptyLine2))
    val nonEmptyLine = " test"
    assert(!preprocessor.findSpaceLines(nonEmptyLine))
  }

  test("getWordCount") {
    val preprocessor = new Preprocessor()
    val stringToTest =
      List("Dit is een testzin, met veel nutteloze.. leestekens?",
      "Zodat we word count kunnen testen.",
      "Als we dit niet doen zijn we nooit zeker of de functie effectief werkt!")
    assert(preprocessor.getWordCount(stringToTest) == 28)
    val stringToTest2 =
      List("Dit zijn nog een paar zinnen..",
      "Kwestie van zeker te zijn?",
      "We zullen ook-tussen de woorden zetten en zelfs- test.")
    assert(preprocessor.getWordCount(stringToTest2) == 21)
  }

  // TODO: add more punctuation marks (spanish, french, german, ...) to test
  test("getPunctuationMarkCount") {
    val preprocessor = new Preprocessor()
    val stringToTest =
      List("Allemaal !? . , - ( ) lees-tekens',",
        "hopelijk \" werkt het ook ")
    assert(preprocessor.getPunctuationMarkCount(stringToTest) == 10)
  }
}

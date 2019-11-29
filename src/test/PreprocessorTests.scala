import org.apache.log4j.helpers.LogLog
import org.scalatest._
import utilities.Preprocessor

class PreprocessorTests extends FunSuite with BeforeAndAfter {

  before {
    // disable logging during testing
    LogLog.setQuietMode(true)
  }

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

  test("logWordCount") {
    val preprocessor = new Preprocessor()
    val stringToTest = Vector("Dit is een testzin, met veel nutteloze.. leestekens?",
      "Zodat we word count kunnen testen.",
      "Als we dit niet doen zijn we nooit zeker of de functie effectief werkt!"
    )
    assert(preprocessor.logWordCount(stringToTest) == 28)
    val stringToTest2 = Vector(
      "Dit zijn nog een paar zinnen..",
      "Kwestie van zeker te zijn?",
      "We zullen ook-tussen de woorden zetten en zelfs- test."
    )
    assert(preprocessor.logWordCount(stringToTest2) == 21)
  }

  // TODO: add more punctuation marks (spanish, french, german, ...) to test
  test("logPunctuationMarkCount") {
    val preprocessor = new Preprocessor()
    val stringToTest =
      Vector("Allemaal !? . , - ( ) lees-tekens',",
        "hopelijk \" werkt het ook ")
    val stringToTest2 = Vector(" ", "", "...",". . . ? ")
    assert(preprocessor.logPunctuationMarkCount(stringToTest) == 11)
    assert(preprocessor.logPunctuationMarkCount(stringToTest2) == 7)
  }

  test("logUppercaseCount") {
    val preprocessor = new Preprocessor()
    val stringToTest = Vector(
      "Dit is EEN RaRe Zin OM capitals te testen. sOmS iS DaT NOdIg...",
      "DDDDDDDDDDDD",
      "",
      "Hello World"
    )
    assert(preprocessor.logUppercaseCount(stringToTest) == 31)
  }

  test("logLowercaseCount") {
    val preprocessor = new Preprocessor()
    val stringToTest = Vector(
      "Dit is een testZin",
      "Dit is EEN RaRe Zin OM capitals te testen. sOmS iS DaT NOdIg...",
      "",
      "Hello World"
    )
    assert(preprocessor.logLowercaseCount(stringToTest) == 51)
  }
}

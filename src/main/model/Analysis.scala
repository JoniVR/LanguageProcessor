package model

object Analysis {
  def getDummy: Analysis = {
    Analysis("TestAnalysis",
      "English",
      Map("A" -> 1, "B" -> 2),
      Map("C" -> 1, "D" -> 2),
      Map("E" -> 0.1, "F" -> 0.2),
      70,
      30,
      Map("AB" -> 0.1, "BC" -> 0.2),
      Map("ABC" -> 0.1, "BCD" -> 0.2),
      Map("A_C" -> 0.1, "B_D" -> 0.2),
      Map("AB" -> 1, "BC" -> 2),
      Map("ABC" -> 1, "BCD" -> 2),
      Map("A_C" -> 1, "B_D" -> 2),
    )
  }
}

case class Analysis
(
  name: String,
  language: String,
  numberOfWordsStartingWithLetter: Map[String, Int],
  numberOfWordsEndingWithLetter: Map[String, Int],
  frequencyOfLetters: Map[String, Double],
  consonantsPercentage: Int,
  vowelsPercentage: Int,
  mostFrequentBiGrams: Map[String, Double],
  mostFrequentTriGrams: Map[String, Double],
  mostFrequentSkipGrams: Map[String, Double],
  mostFrequentBiGramsStartOfWord: Map[String, Int],
  mostFrequentBiGramsEndOfWord: Map[String, Int],
  mostFrequentSkipGramsMatchingBiGramFrequency: Map[String, Double]
)

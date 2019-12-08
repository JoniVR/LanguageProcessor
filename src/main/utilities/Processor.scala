package utilities

import model._

class Processor {

  /**
   * For each letter of the alphabet of the language, count the number of words that starts with or ends with that letter and return as a percentage.
   *
   * Maps the alphabet list to each letter individually and then loops through the text that is split by words.
   * It then counts every word that starts (or ends) with that letter.
   * The result for each letter is converted to a percentage (as double) and then stored inside the resultMap with as key the specific letter
   * and as value the relative occurrence percentage of that letter.
   * @param language The language the vector will be in. This is important because different languages have different alphabets.
   * @param vector A vector where each value equals a line read from the text.
   * @param isStartsWith Indicates whether to change to look for the start or end of the word to match the letters.
   * @return A map with as key the letter of the alphabet for the specific language
   *         and as value the occurrence percentage where that specific letter is the first (or last) letter of a word.
   */
  def calculateStartsOrEndsWithEachLetterOfAlphabetPercentage(language: Languages.Value, vector: Vector[String], isStartsWith: Boolean): Map[Char, Double] = {
    val alphabet = Alphabets.alphabets(language)
    val splitByWords = vector.flatMap(_.split("\\W+"))
    val totalWordCount: Double = splitByWords.length

    val resultMap: Map[Char, Double] = alphabet.map(letter => (letter, {
      val count = splitByWords.count(word => {
        if (isStartsWith) word.toLowerCase.startsWith(letter.toString)
        else word.toLowerCase.endsWith(letter.toString)
      })
      count/totalWordCount * 100
    })).toMap
    resultMap
  }

  def calculateAlphabetLetterFrequencyPercentage(language: Languages.Value, vector: Vector[String]): Map[Char, Double] = {
    val alphabet = Alphabets.alphabets(language)
    val text = vector.mkString.toLowerCase
    // make sure to only count letters that exist in specified language alphabet as letters!
    val totalLetterCount: Double = text.filter(char => alphabet.contains(char)).length
    val resultMap: Map[Char, Double] = alphabet.map(letter => (letter, {
      val count = text.count(_ == letter)
      count/totalLetterCount * 100
    })).toMap
    resultMap
  }
}
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
      count/totalWordCount
    })).toMap
    resultMap
  }

  /**
   * Calculates the frequency (as a percentage) that every letter from the specified alphabet is present in the vector relative to the total amount of characters.
   * Does not count letters that are not part of the specified alphabet.
   * @param language The language the vector will be in. This is important because different languages have different alphabets.
   * @param vector A vector where each value equals a line read from the text.
   * @return A map with as key the letter of the alphabet for the specific language
   *         and as value the occurrence percentage relative to the total amount of characters of that language.
   */
  def calculateAlphabetLetterFrequencyPercentage(language: Languages.Value, vector: Vector[String]): Map[Char, Double] = {
    val alphabet = Alphabets.alphabets(language)
    val text = vector.mkString.toLowerCase
    // make sure to only count letters that exist in specified language alphabet as letters!
    val totalLetterCount: Double = text.filter(char => alphabet.contains(char)).length
    val resultMap: Map[Char, Double] = alphabet.map(letter => (letter, {
      val count = text.count(_ == letter)
      count/totalLetterCount
    })).toMap
    resultMap
  }

  /**
   * Calculates the frequency (as a percentage) of vowels and consonants in a text of a specified language.
   * Note that only valid letters of said alphabet will be counted.
   * @param language The language the vector will be in. This is important because different languages have different alphabets.
   * @param vector A vector where each value equals a line read from the text.
   * @return A map with as key a string indicating what is being returned and as value the frequency percentage (vowel vs consonants).
   */
  def calculateVowelsAndConsonantsFrequencyPercentage(language: Languages.Value, vector: Vector[String]): Map[String, Double] = {
    val text = vector.mkString.toLowerCase
    val validCharsOnly = text.replaceAll(s"[^${Alphabets.alphabets(language)}]", "")
    val totalValidCharCount: Double = validCharsOnly.length
    val vowelCount: Double = validCharsOnly.replaceAll(s"[^${Alphabets.vowels(language)}]","").length
    val consonantCount: Double = totalValidCharCount - vowelCount
    Map("vowels" -> vowelCount/totalValidCharCount, "consonants" -> consonantCount/totalValidCharCount)
  }
}
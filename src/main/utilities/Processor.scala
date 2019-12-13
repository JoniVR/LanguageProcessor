package utilities

import model._

object Processor {

  /**
   * Function that handles the various processing operations.
   * @param vector The vector containing the text, each value should be a line from the text.
   * @param language The specified language of the text.
   *                 This is important as non-alphabet characters for that language will be filtered out.
   */
  def processText(vector: Vector[String], language: Languages.Value): Unit = {
    val filteredVector = filterNonAlphabetCharacters(vector, language)
    
  }

  /**
   * Responsible for filtering all characters that are not in the specified alphabet.
   * This is necessary so we don't count punctuation marks or invalid characters in our analysis.
   * Removing unnecessary characters beforehand should also speed up processing time significantly.
   * @param vector The raw text vector. Each value represents a line from the text.
   * @param language The specified language of the text. We use this to filter out any non-alphabet characters.
   * @return Returns a new vector where all characters except for alphabet characters and spaces are removed.
   */
  def filterNonAlphabetCharacters(vector: Vector[String], language: Languages.Value): Vector[String] = {
    vector.map(_.toLowerCase.replaceAll(s"[^${Alphabets.alphabets(language)} ]", ""))
  }

  /**
   * For each letter of the alphabet of the language, count the number of words that starts with or ends with that letter and return as a percentage.
   *
   * Maps the alphabet list to each letter individually and then loops through the text that is split by words.
   * It then counts every word that starts (or ends) with that letter.
   * The result for each letter is converted to a percentage (as double) and then stored inside the resultMap with as key the specific letter
   * and as value the relative occurrence percentage of that letter.
   * @param language The language the vector will be in. This is important because different languages have different alphabets.
   * @param vector A vector where each value equals a line read from the text. This should be filtered already to only contain valid chars.
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
        if (isStartsWith) word.startsWith(letter.toString)
        else word.endsWith(letter.toString)
      })
      count/totalWordCount
    })).toMap
    resultMap
  }

  /**
   * Calculates the frequency (as a percentage) that every letter from the specified alphabet is present in the vector relative to the total amount of characters.
   * Does not count letters that are not part of the specified alphabet.
   * @param language The language the vector will be in. This is important because different languages have different alphabets.
   * @param vector A vector where each value equals a line read from the text. This should be filtered already to only contain valid chars.
   * @return A map with as key the letter of the alphabet for the specific language
   *         and as value the occurrence percentage relative to the total amount of characters of that language.
   */
  def calculateAlphabetLetterFrequencyPercentage(language: Languages.Value, vector: Vector[String]): Map[Char, Double] = {
    val alphabet = Alphabets.alphabets(language)
    val text = vector.mkString
    // make sure to only count letters that exist in specified language alphabet as letters!
    val totalLetterCount: Double = text.filterNot(_.equals(' ')).length
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
   * @param vector A vector where each value equals a line read from the text. This should be filtered already to only contain valid chars.
   * @return A map with as key a string indicating what is being returned and as value the frequency percentage (vowel vs consonants).
   */
  def calculateVowelsAndConsonantsFrequencyPercentage(language: Languages.Value, vector: Vector[String]): Map[String, Double] = {
    val text = vector.mkString.filterNot(_.equals(' '))
    val totalValidCharCount: Double = text.length
    val vowelCount: Double = text.replaceAll(s"[^${Alphabets.vowels(language)}]","").length
    val consonantCount: Double = totalValidCharCount - vowelCount
    Map("vowels" -> vowelCount/totalValidCharCount, "consonants" -> consonantCount/totalValidCharCount)
  }
}
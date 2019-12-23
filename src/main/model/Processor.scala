package model

object Processor {

  /**
   * Function that handles the various processing operations.
   * @param vector The vector containing the text, each value should be a line from the text.
   * @param language The specified language of the text.
   *                 This is important as non-alphabet characters for that language will be filtered out.
   * @return Returns an object of type `Analysis` used for writing data to json.
   */
  def processText(vector: Vector[String], name: String, language: Languages.Value): Analysis = {
    val filteredVector = filterNonAlphabetCharacters(vector, language)

    val vowelsAndConsonantsPercentage = calculateVowelsAndConsonantsPercentage(language, filteredVector)
    val mostFrequentBigramsAndSkipgrams = calculateTop25BigramAndTrigramPercentage(filteredVector)

    Analysis(
      name,
      language.toString,
      calculateStartsOrEndsWithEachLetterOfAlphabetPercentage(language, filteredVector, isStartsWith = true),
      calculateStartsOrEndsWithEachLetterOfAlphabetPercentage(language, filteredVector, isStartsWith = false),
      calculateAlphabetLetterPercentage(language, filteredVector),
      vowelsAndConsonantsPercentage("vowels"),
      vowelsAndConsonantsPercentage("consonants"),
      mostFrequentBigramsAndSkipgrams._1,
      mostFrequentBigramsAndSkipgrams._2,
      calculateTop25SkipgramPercentage(filteredVector),
      calculateNumberOfWordsStartingOrEndingWithTop25Bigrams(filteredVector, isStartsWith = true),
      calculateNumberOfWordsStartingOrEndingWithTop25Bigrams(filteredVector, isStartsWith = false),
      calculateBigramAndSkipgramMatchingPercentage(filteredVector)
    )
  }

  /**
   * Responsible for filtering all characters that are not in the specified alphabet.
   * This is necessary so we don't count punctuation marks or invalid characters in our analysis.
   * Removing unnecessary characters beforehand should also speed up processing time significantly.
   * @param nonFilteredVector The raw text vector. Each value represents a line from the text.
   * @param language The specified language of the text. We use this to filter out any non-alphabet characters.
   * @return Returns a new vector where all characters except for alphabet characters and spaces are removed.
   */
  def filterNonAlphabetCharacters(nonFilteredVector: Vector[String], language: Languages.Value): Vector[String] = {
    nonFilteredVector.map(_.toLowerCase.replaceAll(s"[^${Alphabets.alphabets(language)} ]", ""))
  }

  /**
   * For each letter of the alphabet of the language, count the number of words that starts with or ends with that letter and return as a percentage.
   *
   * Maps the alphabet list to each letter individually and then loops through the text that is split by words.
   * It then counts every word that starts (or ends) with that letter.
   * The result for each letter is converted to a percentage (as double) and then stored inside the resultMap with as key the specific letter
   * and as value the relative occurrence percentage of that letter.
   * @param language The language the vector will be in. This is important because different languages have different alphabets.
   * @param filteredVector A vector where each value equals a line read from the text. This should be filtered already to only contain valid chars.
   * @param isStartsWith Indicates whether to look for the start or end of the word to match the letters.
   * @return A map with as key the letter of the alphabet for the specific language
   *         and as value the occurrence percentage where that specific letter is the first (or last) letter of a word.
   */
  def calculateStartsOrEndsWithEachLetterOfAlphabetPercentage(language: Languages.Value, filteredVector: Vector[String], isStartsWith: Boolean): Map[String, Double] = {
    val alphabet = Alphabets.alphabets(language)
    val splitByWords = filteredVector.flatMap(_.split("\\W+"))
    val totalWordCount: Double = splitByWords.length

    val resultMap = alphabet.map(letter => (letter.toString, {
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
   * @param filteredVector A vector where each value equals a line read from the text. This should be filtered already to only contain valid chars.
   * @return A map with as key the letter of the alphabet for the specific language
   *         and as value the occurrence percentage relative to the total amount of characters of that language.
   */
  def calculateAlphabetLetterPercentage(language: Languages.Value, filteredVector: Vector[String]): Map[String, Double] = {
    val alphabet = Alphabets.alphabets(language)
    val text = filteredVector.mkString
    // make sure to only count letters that exist in specified language alphabet as letters!
    val totalLetterCount: Double = text.filterNot(_.equals(' ')).length
    val resultMap = alphabet.map(letter => (letter.toString, {
      val count = text.count(_ == letter)
      count/totalLetterCount
    })).toMap
    resultMap
  }

  /**
   * Calculates the frequency (as a percentage) of vowels and consonants in a text of a specified language.
   * Note that only valid letters of said alphabet will be counted.
   * @param language The language the vector will be in. This is important because different languages have different alphabets.
   * @param filteredVector A vector where each value equals a line read from the text. This should be filtered already to only contain valid chars.
   * @return A map with as key a string indicating what is being returned and as value the frequency percentage (vowel vs consonants).
   */
  def calculateVowelsAndConsonantsPercentage(language: Languages.Value, filteredVector: Vector[String]): Map[String, Double] = {
    val text = filteredVector.mkString.filterNot(_.equals(' '))
    val totalValidCharCount: Double = text.length
    val vowelCount: Double = text.replaceAll(s"[^${Alphabets.vowels(language)}]","").length
    val consonantCount: Double = totalValidCharCount - vowelCount
    Map("vowels" -> vowelCount/totalValidCharCount, "consonants" -> consonantCount/totalValidCharCount)
  }

  /**
   * Calculates the frequency (as a percentage) of times one of the top 25 bigrams
   * in the text is part of the start (or end, depending on `isStartsWith`) of a word.
   * @param filteredVector A vector where each value equals a line read from the text. This should be filtered already to only contain valid chars.
   * @param isStartsWith Indicates whether to look for the start or end of the word to match the letters.
   * @return A map with as key the bigram and as value the number of words that start or end with said bigram.
   */
  def calculateNumberOfWordsStartingOrEndingWithTop25Bigrams(filteredVector: Vector[String], isStartsWith: Boolean): Map[String, Int] = {
    val bigrams = NGramsAnalyser.getNgrams(filteredVector).take(25).keys.toList
    val splitByWords = filteredVector.flatMap(_.split("\\W+"))

    bigrams
      .map(value => (value, {
        if (isStartsWith) splitByWords.count(_.startsWith(value))
        else splitByWords.count(_.endsWith(value))
      }))
      .toMap
  }

  /**
   * Calculates the frequency (as a percentage) of the top 25 bigrams and trigrams relative to the total bigram or trigram count and returns both as maps inside a tuple.
   * @param filteredVector A vector where each value equals a line read from the text. This should be filtered already to only contain valid chars.
   * @return A tuple with as first value a map of bigrams with as key the bigram and as value the relative occurrence compared to the total amount of bigrams in the text.
   *         and as second value a map of trigrams with as key the trigram and as value the relative occurrence compared to the total amount of trigrams in the text.
   */
  def calculateTop25BigramAndTrigramPercentage(filteredVector: Vector[String]): (Map[String, Double], Map[String, Double]) = {
    val bigrams = NGramsAnalyser.getNgrams(filteredVector)
    val trigrams = NGramsAnalyser.getNgrams(filteredVector, 3)
    // get sum of map values
    val bigramCount: Double = bigrams.foldLeft(0)(_+_._2)
    val trigramCount: Double = trigrams.foldLeft(0)(_+_._2)

    val bigramMap = bigrams.take(25).transform((_,v) => v/bigramCount)
    val trigramMap = trigrams.take(25).transform((_,v) => v/trigramCount)
    (bigramMap, trigramMap)
  }

  /**
   * Calculates the frequency (as a percentage) of the top 25 skipgrams relative to the total skipgram count and returns them as a map.
   * For each value in the skipgram map, the subsequent bigram count is fetched and the relative frequency is also returned as a map.
   * Both of these maps are then returned inside a Tuple(SkipGramMap, BiGramMap)
   * @param filteredVector A vector where each value equals a line read from the text. This should be filtered already to only contain valid chars.
   * @return A map containing the skipgram names as keys and the frequency relative to the total word count for each as values.
   */
  def calculateTop25SkipgramPercentage(filteredVector: Vector[String]): Map[String, Double] = {
    val skipGrams = NGramsAnalyser.getSkipGrams(filteredVector)
    val skipGramCount: Double = skipGrams.foldLeft(0)(_+_._2)
    skipGrams.take(25).transform((_, v) => v/skipGramCount)
  }

  /**
   * Calculates the frequency (as a percentage) of the bigram for all matching top 25 skipgrams.
   * Returns both as maps with frequencies (relative to the total amount of bigrams/skipgrams) inside a Tuple(SkipGramMap, BiGramMap).
   *
   * @param filteredVector A vector where each value equals a line read from the text. This should be filtered already to only contain valid chars.
   * @return A tuple with as first value a map of the top 25 skipgrams with as key the skipgram and as value the relative occurrence compared to the total amount of skipgrams in the text.
   *         and as second value a map of bigrams that match the skipgrams with as key the bigram and as value the relative occurrence compared to the total amount of bigrams in the text.
   */
  def calculateBigramAndSkipgramMatchingPercentage(filteredVector: Vector[String]): (Map[String, Double], Map[String, Double]) = {
    val totalBiGramCount: Double = NGramsAnalyser.getNgrams(filteredVector).foldLeft(0)(_+_._2)
    val skipGrams = calculateTop25SkipgramPercentage(filteredVector)

    val biGrams = skipGrams.map(v => (v._1.replaceAll("_",""), {
      val nGram = v._1.replaceAll("_","")
      val biGramCount = NGramsAnalyser.getNgramCount(filteredVector, nGram)
      biGramCount/totalBiGramCount
    }))
    (skipGrams, biGrams)
  }
}

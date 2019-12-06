package utilities

import model._

class Processor {

  /**
   * For each letter of the alphabet of the language, count the number of words that starts with that letter.
   * @param language The language the vector will be in. This is important because different languages have different alphabets.
   * @param vector A vector where each value equals a line read from the text.
   * @return An optional map with as key the letter of the alphabet for the specific language
   *         and as value the percentage that that specific letter is the first letter of a word.
   *         If the language specified is not found an empty optional (Option.empty) will be returned.
   */
  def getStartsWithEachLetterOfAlphabetPercentage(language: Languages.Value, vector: Vector[String]): Option[Map[Char, Int]] = {
    val alphabet = Alphabets.alphabets.get(language)
    if (alphabet.isEmpty) return Option.empty
    val splitByWords = vector.flatMap(_.split("\\W+"))
    val totalWordCount = splitByWords.length

    val resultMap: Map[Char, Int] = Map()
    alphabet.get.foreach(
      letter => {
        val count = splitByWords.count(word => word.toLowerCase.startsWith(letter.toString))
        // add to immutable map
        print(count)
        resultMap.updated(letter, count/totalWordCount)
      }
    )
    Option(resultMap)
  }
}

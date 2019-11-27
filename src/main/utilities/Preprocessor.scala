package utilities
import org.apache.log4j.{Level, Logger}

class Preprocessor {

  val logger: Logger = Logger.getLogger(this.getClass.getName)

  def removeSpaces(line: String): String = {
    // NOTE: You can ignore red line in IntelliJ, known bug: https://youtrack.jetbrains.com/issue/IDEA-219322
    // regex makes use of Positive lookahead
    // first we get out the most of the spaces by using this regex as it's a lot more performant,
    val transformedLine = line.replaceAll("\\s+(?=\\p{Punct})", "")
    // after that we manually replace some leftovers
    val manualReplacements = Map(
      "( " -> "(",
      "' " -> "'",
      "/ " -> "/",
      "\" " -> "\"",
      "\"" -> " \"",
      "- " -> "-"
    )
    // traverse all replacements from left to right (`foldleft`) and apply `replaceAllLiterally` as operator to each one.
    manualReplacements.foldLeft(transformedLine)((a, b) => a.replaceAllLiterally(b._1, b._2))
  }

  def findSpaceLines(line: String): Boolean = {
    line.isBlank
  }

  /**
   * @param vector The lines we read from the file.
   * @return The number of words in the text. We return a value here for testing purposes.
   */
  def logWordCount(vector: Vector[String]): Long = {
    val wordCount = vector
      .flatMap(_.split("\\W+"))
      .length
    logger.info("Word Count: " + wordCount)
    wordCount
  }

  /**
   * @param vector The lines we read from the file.
   * @return The number of punctuation marks in the text. We return a value here for testing purposes.
   */
  def logPunctuationMarkCount(vector: Vector[String]): Long = {
    val stringToMatch = vector.mkString
    val regex = "\\p{Punct}"
    val punctuationCount = regex.r.findAllIn(stringToMatch).length
    logger.info("Punctuation marks Count: " + punctuationCount)
    punctuationCount
  }

  /**
   * @param vector The lines we read from the file.
   * @return The number of uppercase letters in the text. We return a value here for testing purposes.
   */
  def logUppercaseCount(vector: Vector[String]): Long = {
    vector.mkString.count(c => c.isUpper)
  }

  /**
   * @param vector The lines we read from the file.
   * @return The number of lowercase letters in the text. We return a value here for testing purposes.
   */
  def logLowercaseCount(vector: Vector[String]): Long = {
    vector.mkString.count(c => c.isLower)
  }
}

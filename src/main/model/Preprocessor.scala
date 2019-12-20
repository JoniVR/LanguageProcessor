package model

import org.apache.log4j.Logger

object Preprocessor {

  private val logger: Logger = Logger.getLogger(this.getClass.getName)

  def doLogging(vector: Vector[String], fileName: String): Unit = {
    logger.info("--------- START RUN ---------")
    logger.info(s"Filename: '$fileName'")
    logWordCount(vector)
    logPunctuationMarkCount(vector)
    logUppercaseCount(vector)
    logLowercaseCount(vector)
    logger.info("--------- END RUN ---------")
  }

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
    line.trim.isEmpty
  }

  /**
   * @param vector The lines we read from the file.
   * @return The number of words in the text. We return a value here for testing purposes.
   */
  def logWordCount(vector: Vector[String]): Long = {
    val wordCount = vector
      .flatMap(_.split("\\W+"))
      .length
    logger.info(s"Word Count: $wordCount")
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
    logger.info(s"Punctuation marks Count: $punctuationCount")
    punctuationCount
  }

  /**
   * @param vector The lines we read from the file.
   * @return The number of uppercase letters in the text. We return a value here for testing purposes.
   */
  def logUppercaseCount(vector: Vector[String]): Long = {
    val upperCount = vector.mkString.count(_.isUpper)
    logger.info(s"Uppercase letter Count: $upperCount")
    upperCount
  }

  /**
   * @param vector The lines we read from the file.
   * @return The number of lowercase letters in the text. We return a value here for testing purposes.
   */
  def logLowercaseCount(vector: Vector[String]): Long = {
    val lowerCount = vector.mkString.count(_.isLower)
    logger.info(s"Lowercase letter Count: $lowerCount")
    lowerCount
  }
}

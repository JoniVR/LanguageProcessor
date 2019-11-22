package utilities

class Preprocessor {

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

  // TODO: actually log instead of return
  def getWordCount(list: List[String]): Long = {
    list
      .flatMap(_.split("\\W+"))
      .length
  }

  // TODO: actually log instead of return
  // TODO: Actually probably needs fixing too.
  def getPunctuationMarkCount(list: List[String]): Long = {
    val regex = "\\s+(?=\\p{Punct})".r
    list.filterNot(regex.matches(_)).length
  }
}

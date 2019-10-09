package utilities

class Preprocessor {

  def removeSpaces(line: String): String = {
    //NOTE: You can ignore red line in IntelliJ, known bug: https://youtrack.jetbrains.com/issue/IDEA-219322
    // regex makes use of Positive lookahead
    line.replaceAll("\\s+(?=\\p{Punct})", "")
  }

  def findSpaceLines(line: String): Boolean = {
    line.isBlank
  }
}

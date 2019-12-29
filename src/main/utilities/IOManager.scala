package utilities

import java.nio.file.{Files, Paths}

import model.Analysis
import spray.json._
import utilities.AnalysisJsonProtocol._

class IOManager {
  @throws(classOf[Exception])
  def readFile(filePath: String): Vector[String] = {
    var bufferedSource: io.Source = null
    try {
      bufferedSource = io.Source.fromFile(filePath)
      val lines = bufferedSource.getLines().toVector
      lines
    } catch {
      case ex: Exception =>
        throw ex
    } finally {
      bufferedSource.close
    }
  }

  /**
   * Writes the given analysis in a pretty-printed format to a new file.
   * @note output is in the project subdirectory /analysis.
   * @param fileName name of the file to create.
   * @param analysis result of the completed analysis.
   */
  def writeAnalysis(fileName: String, analysis: Analysis): Unit = {
    val content = analysis.toJson.prettyPrint
    val URI = System.getProperty("user.dir")
    val path = Paths.get(URI.toString, s"/analysis/$fileName.json")
    Files.write(path, content.getBytes())
  }

  /**
   * Reads the given file and parses it to an analysis object.
   * @param filePath the file to read.
   * @return analysis object with values found in the given file.
   */
  def readAnalysis(filePath: String): Analysis = {
    readFile(filePath)
      .mkString
      .parseJson
      .convertTo[Analysis]
  }
}

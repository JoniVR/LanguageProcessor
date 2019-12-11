package utilities

import spray.json._
import Dto.AnalysisDto
import utilities.AnalysisJsonProtocol._


class IOManager
{
  @throws(classOf[Exception])
  def readFile(filePath: String): Vector[String] = {
    var bufferedSource:io.Source = null
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

  def writeAnalysisDto(filePath: String, analysis: AnalysisDto): Unit = {
    val dtoString = analysis.toJson
    println(dtoString.prettyPrint)
  }

  def readAnalysisDto(filePath: String): AnalysisDto = {
    AnalysisDto.getTestDto
  }

}

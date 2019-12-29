package model

import javafx.concurrent.{Service, Task}

class AnalysisService(val lines: Vector[String], filename: String, language: Languages.Value) extends Service[Analysis] {
  val processor = new Processor
  val preprocessor = new Preprocessor

  override def createTask(): Task[Analysis] = () => {
    val processedList =
      lines.view.filter(!preprocessor.findSpaceLines(_))
        .map(preprocessor.removeSpaces)
        .to(Vector)
    preprocessor.doLogging(processedList, filename)
    processor.processText(processedList, filename, language)
  }
}

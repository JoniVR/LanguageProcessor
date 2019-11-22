package presenter

;

import utilities.{IOManager, Preprocessor};

// NOTE: Kan zijn dat dit samen kan met andere presenter
class PreprocessorPresenter {

  val preprocessor = new Preprocessor()

  // TODO: Rename / respond to event
  def fileUploadClicked() {
    // TODO: Get path from UI and pass to `readFile`
    val ioManager = new IOManager()
    val fileIterator = ioManager.readFile("files/europarl/dutch/Alldata Dutch.txt")
    if (fileIterator.isDefined) {
      // preprocessing spaces, loading processed list in memory (only once!)
      val processedList = fileIterator.get
        .filter(!preprocessor.findSpaceLines(_))
        .map(preprocessor.removeSpaces)
        .to(List)
      preprocessor.getWordCount(processedList)
      preprocessor.getPunctuationMarkCount(processedList)
    }
  }
}

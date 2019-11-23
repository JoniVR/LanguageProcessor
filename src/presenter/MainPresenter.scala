package presenter

import javafx.fxml.FXML
import javafx.scene.control.MenuItem
import javafx.stage.{FileChooser, Stage}
import utilities.{IOManager, Preprocessor}

class MainPresenter {
  @FXML private var uploadMenuItem: MenuItem = _
  @FXML private var preferencesMenuItem: MenuItem = _
  @FXML private var aboutMenuItem: MenuItem = _

  private val preprocessor = new Preprocessor()

  @FXML
  def uploadMenuClicked(): Unit = {
    val ioManager = new IOManager()
    val fileChooser = new FileChooser
    try {
      val files = fileChooser.showOpenMultipleDialog(new Stage())
      files.forEach(f => {
        val fileIterator = ioManager.readFile(f.getPath)
        preProcessFile(fileIterator)
      })
    }
    catch {
      case ex: Exception =>
        // TODO: Show error UI for different exceptions and log it
        // NOTE: "cancel" klikken geeft ook een exception!
        ex.printStackTrace()
    }
  }

  @FXML
  def preferencesMenuClicked(): Unit = {
    println("preferences clicked")
  }

  @FXML
  def aboutMenuClicked(): Unit = {
    println("about clicked")
  }

  private def preProcessFile(it: Iterator[String]): Unit = {
    // preprocessing spaces, loading processed list in memory (only once!)
    val processedList =
      it.filter(!preprocessor.findSpaceLines(_))
        .map(preprocessor.removeSpaces)
        .to(List)
    // TODO: actually log
    println(processedList)
    preprocessor.getWordCount(processedList)
    preprocessor.getPunctuationMarkCount(processedList)
  }
}

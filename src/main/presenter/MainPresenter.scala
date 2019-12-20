package presenter

import javafx.fxml.FXML
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.{Alert, ButtonType, MenuItem}
import javafx.scene.layout.Region
import javafx.stage.{FileChooser, Stage}
import org.apache.log4j.Logger
import utilities.{IOManager}
import model.{Languages, Preprocessor, Processor}

class MainPresenter {
  @FXML private var uploadMenuItem: MenuItem = _
  @FXML private var preferencesMenuItem: MenuItem = _
  @FXML private var aboutMenuItem: MenuItem = _

  private val logger: Logger = Logger.getLogger(this.getClass.getName)

  @FXML
  def uploadMenuClicked(): Unit = {
    val fileChooser = new FileChooser
    try {
      val files = fileChooser.showOpenMultipleDialog(new Stage())
      if (files != null) {
        files.forEach(f => {
          val fileVector = IOManager.readFile(f.getPath)
          preProcessFile(fileVector, f.getName)
          // TODO: Change Language parameter to be dynamic based on selection
          val analysis = Processor.processText(fileVector, Languages.Dutch)
          IOManager.writeAnalysis(analysis.name, analysis)
        })
      }
    }
    catch {
      case ex: Exception =>
        ex.printStackTrace()
        val alert = new Alert(AlertType.ERROR, "Error trying to load file!", ButtonType.OK)
        alert.getDialogPane.setMinHeight(Region.USE_PREF_SIZE)
        alert.show()
        logger.error(ex)
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

  private def preProcessFile(vector: Vector[String], filename: String): Unit = {
    // using view on a vector is a lot more memory efficient compared to using a list and stream
    // see: https://docs.scala-lang.org/tutorials/FAQ/stream-view-iterator.html
    val processedList =
      vector.view.filter(!Preprocessor.findSpaceLines(_))
        .map(Preprocessor.removeSpaces)
        .to(Vector)
    Preprocessor.doLogging(processedList, filename)
  }
}

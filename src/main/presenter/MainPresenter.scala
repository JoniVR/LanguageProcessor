package presenter

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.{Alert, ButtonType, MenuItem, Tab, TabPane}
import javafx.scene.layout.{GridPane, Region}
import javafx.stage.{FileChooser, Stage}
import model.Analysis
import org.apache.log4j.Logger
import utilities.{IOManager}
import model.{Languages, Preprocessor, Processor}

class MainPresenter {
  @FXML private var newAnalysisMenuItem: MenuItem = _
  @FXML private var openAnalysisMenuItem: MenuItem = _
  @FXML private var preferencesMenuItem: MenuItem = _
  @FXML private var aboutMenuItem: MenuItem = _
  @FXML private var analysisTabPane: TabPane = _

  private val logger: Logger = Logger.getLogger(this.getClass.getName)
  private val fileChooser: FileChooser = new FileChooser

  @FXML
  def newAnalysisMenuClicked(): Unit = {
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
      case ex: Exception => showErrorDialog(ex)
    }
  }

  @FXML
  def openAnalysisMenuClicked(): Unit = {
    try {
      val files = fileChooser.showOpenMultipleDialog(new Stage())
      files.forEach(f => {
        val analysis = IOManager.readAnalysis(f.getPath)
        openNewAnalysisTab(analysis)
      })
    }
    catch {
      case ex: Exception => showErrorDialog(ex)
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


  private def openNewAnalysisTab(analysis: Analysis): Unit = {
    val tab = new Tab(analysis.name)
    val loader = new FXMLLoader(getClass.getResource("/view/AnalysisTabContent.fxml"))
    val content: GridPane = loader.load
    val controller: AnalysisTabPresenter = loader.getController
    controller.loadAnalysis(analysis)

    tab.setContent(content)
    analysisTabPane.getTabs.add(tab)
  }

  /**
   * Common error dialog for different uploads
   */
  private def showErrorDialog(ex: Exception): Unit = {
    val errorDialog: Alert = new Alert(AlertType.ERROR, "Error trying to load file!", ButtonType.OK)
    errorDialog.getDialogPane.setMinHeight(Region.USE_PREF_SIZE)
    errorDialog.show()
    logger.error(ex)
    ex.printStackTrace()
  }
}

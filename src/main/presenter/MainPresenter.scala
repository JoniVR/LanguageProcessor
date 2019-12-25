package presenter

import java.util.Optional

import exception.LanguageNotSupportedException
import javafx.collections.{FXCollections, ObservableList}
import javafx.fxml.{FXML, FXMLLoader}
import javafx.geometry.Insets
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonBar.ButtonData
import javafx.scene.control.{Alert, ButtonType, ComboBox, Dialog, Label, MenuItem, Tab, TabPane, TextField}
import javafx.scene.layout.{BorderPane, GridPane, Region}
import javafx.stage.{FileChooser, Stage}
import javafx.util.Pair
import model.Analysis
import org.apache.log4j.Logger
import utilities.IOManager
import model.{Languages, Preprocessor, Processor}

class MainPresenter {
  @FXML private var newAnalysisMenuItem: MenuItem = _
  @FXML private var openAnalysisMenuItem: MenuItem = _
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
          val result = showAnalysisOptionDialog(f.getName)
          val options = result.orElseThrow(() => new NoSuchElementException("No analysis options were given"))
          val name = options.getKey
          val language = Languages.withName(options.getValue)
          if (language == null) throw LanguageNotSupportedException("Language is not supported.")

          val fileVector = IOManager.readFile(f.getPath)
          preProcessFile(fileVector, options.getKey)

          /*
          TODO: Fix user having to close program before analysis file is written
                and thus being unable to select analysis after processing...
           */
          val analysis = Processor.processText(fileVector, name, language)
          IOManager.writeAnalysis(analysis.name, analysis)
          openNewAnalysisTab(analysis)
        })
      }
    }
    catch {
      case ex: Exception => showErrorDialog(ex)
    }
  }

  // TODO: better error handling in case of pressing cancel?
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
  def aboutMenuClicked(): Unit = {
    println("about clicked")
  }

  /**
   * Starts the preprocessing of the uploaded file
   * @param vector text in the given file as a vector of strings
   * @param filename ...
   */
  private def preProcessFile(vector: Vector[String], filename: String): Unit = {
    // using view on a vector is a lot more memory efficient compared to using a list and stream
    // see: https://docs.scala-lang.org/tutorials/FAQ/stream-view-iterator.html
    val processedList =
      vector.view.filter(!Preprocessor.findSpaceLines(_))
        .map(Preprocessor.removeSpaces)
        .to(Vector)
    Preprocessor.doLogging(processedList, filename)
  }

  /**
   * Initializes a new tab for the given analysis and places it in the tab pane
   * @param analysis the completed analysis to visualize
   */
  private def openNewAnalysisTab(analysis: Analysis): Unit = {
    val tab = new Tab(analysis.name)
    val loader = new FXMLLoader(getClass.getResource("/view/AnalysisTabContent.fxml"))
    val content: BorderPane = loader.load
    val controller: AnalysisTabPresenter = loader.getController
    controller.loadAnalysis(analysis)

    tab.setContent(content)
    analysisTabPane.getTabs.add(tab)
    analysisTabPane.getSelectionModel.select(tab)
  }

  /**
   * Common error dialog
   */
  private def showErrorDialog(ex: Exception): Unit = {
    val errorDialog: Alert = new Alert(AlertType.ERROR, "Error trying to load file!", ButtonType.OK)
    errorDialog.getDialogPane.setMinHeight(Region.USE_PREF_SIZE)
    errorDialog.show()
    logger.error(ex)
    ex.printStackTrace()
  }

  /**
   * Show a popup used to select the language and give a name to the analysis
   * @param filename name of the uploaded file
   * @return Tuple consisting of the language and a name for the analysis
   */
  private def showAnalysisOptionDialog(filename: String): Optional[Pair[String, String]] = {
    val optionDialog = new Dialog[Pair[String, String]]

    val startButtonType = new ButtonType("Start analysis", ButtonData.OK_DONE)
    optionDialog.getDialogPane.getButtonTypes.addAll(startButtonType, ButtonType.CANCEL)

    val grid = new GridPane
    grid.setHgap(10)
    grid.setVgap(10)
    grid.setPadding(new Insets(20, 150, 10, 10))

    val nameField = new TextField
    nameField.setPromptText("Name")
    // set default analysis name to filename without extension
    nameField.setText(filename.replaceAll("\\.[^.]*$", ""))

    val languageOptions = FXCollections.observableArrayList[String]()
    Languages.values
      .map(_.toString)
      .foreach(lang =>
        languageOptions.add(lang)
      )
    val languageComboBox = new ComboBox(languageOptions)
    languageComboBox.setPromptText("Language")
    languageComboBox.setValue("Dutch")

    grid.add(new Label("Name:"), 0, 0)
    grid.add(nameField, 1, 0)
    grid.add(new Label("Language:"), 0, 1)
    grid.add(languageComboBox, 1, 1)

    optionDialog.setTitle("Analysis options.")
    optionDialog.setHeaderText("Please configure the analysis.")
    optionDialog.getDialogPane.setContent(grid)

    optionDialog.setResultConverter(dialogButton => {
      if (dialogButton == startButtonType) {
        new Pair(nameField.getText, languageComboBox.getValue)
      } else {
       null
      }
    })

    val result = optionDialog.showAndWait()
    result
  }
}

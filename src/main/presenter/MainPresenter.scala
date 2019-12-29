package presenter

import java.util.Optional

import exception.LanguageNotSupportedException
import javafx.collections.FXCollections
import javafx.concurrent.{Service, Task}
import javafx.fxml.{FXML, FXMLLoader}
import javafx.geometry.Insets
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonBar.ButtonData
import javafx.scene.control._
import javafx.scene.layout.{BorderPane, GridPane, Region}
import javafx.stage.{FileChooser, Stage}
import javafx.util.Pair
import model.{Analysis, Languages, Preprocessor, Processor}
import org.apache.log4j.Logger
import utilities.IOManager

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
          val filename = options.getKey
          val language = Languages.withName(options.getValue)
          if (language == null) throw LanguageNotSupportedException("Language is not supported.")

          val lines = IOManager.readFile(f.getPath)
          val service = new Service[Analysis] {
            override def createTask(): Task[Analysis] = () => {
              val processedList =
                lines.view.filter(!Preprocessor.findSpaceLines(_))
                  .map(Preprocessor.removeSpaces)
                  .to(Vector)
              Preprocessor.doLogging(processedList, filename)
              Processor.processText(processedList, filename, language)
            }
          }
          val runningAlert = createAnalysisRunningDialog(filename, language, service)
          service.setOnRunning(_ => runningAlert.showAndWait())
          service.setOnSucceeded(_ => {
            runningAlert.close()
            val analysis = service.getValue
            openNewAnalysisTab(analysis)
            IOManager.writeAnalysis(filename, analysis)
          })
          service.setOnFailed(_ => {
            showErrorDialog(new Exception("Analysis failed."))
          })
          service.setOnCancelled(_ => {
            println("Analysis cancelled!")
          })
          service.start()
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
      if (files != null) {
        files.forEach(f => {
          val analysis = IOManager.readAnalysis(f.getPath)
          openNewAnalysisTab(analysis)
        })
      }
    }
    catch {
      case ex: Exception => showErrorDialog(ex)
    }
  }

  @FXML
  def aboutMenuClicked(): Unit = {
    val alert = new Alert(AlertType.INFORMATION)
    alert.setTitle("About")
    alert.setHeaderText("NGram Analyser")
    val about =
    """
        |This application was developed by:
        |Joni Van Roost
        |Toon Van Deuren
        |
        |Application Development at
        |Karel de Grote Hogeschool, Antwerp
        |""".stripMargin
    alert.setContentText(about)
    alert.show()
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
    val filenameWithoutExtension = Option(filename.replaceAll("\\.[^.]*$", ""))
    nameField.setPromptText(filenameWithoutExtension.getOrElse("Name"))

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
        val analysisName = if (nameField.getText != null && nameField.getText.length > 0) nameField.getText else nameField.getPromptText
        new Pair(analysisName, languageComboBox.getValue)
      } else {
       null
      }
    })

    val result = optionDialog.showAndWait()
    result
  }

  def createAnalysisRunningDialog(name: String, language: Languages.Value, service: Service[Analysis]): Dialog[Void] = {
    val dialog = new Dialog[Void]
    dialog.setTitle("Analysis in progress")
    dialog.setHeaderText("A new file is being analysed.\nThis might take a while.")
    dialog.setContentText(s"Name: $name\nLanguage: $language")

    val cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE)
    dialog.getDialogPane.getButtonTypes.add(cancelButtonType)
    dialog.setResultConverter(button => {
      if (button == cancelButtonType) {
        service.cancel()
      }
      null
    })
    dialog.setOnCloseRequest(_ => service.cancel())

    val progress = new ProgressIndicator
    progress.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS)
    dialog.setGraphic(progress)

    dialog
  }
}

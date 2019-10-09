import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import presenter.NGramsPresenter
import view.NGramsView
import utilities.{IOManager, Preprocessor}

object Main
{
  def main(args: Array[String])
  {
    Application.launch(classOf[Main], args: _*)
  }
}

class Main extends Application
{
  override def start(primaryStage: Stage)
  {
    val nGramsPresenter = new NGramsPresenter
    val nGramsView = new NGramsView(nGramsPresenter)

    val scene: Scene = new Scene(nGramsView)
    // this.getClass.getResource("/resources/style.css").toExternalForm

    // TODO: temporarily placed here, find better place in future
    val ioManager = new IOManager()
    val preprocessor = new Preprocessor()
    val fileIterator = ioManager.readFile("files/europarl/dutch/Alldata Dutch.txt")
    if (fileIterator.isDefined) {
      fileIterator.get
        .map(preprocessor.removeSpaces)
        .filter(!preprocessor.findSpaceLines(_))
        .foreach(println)
    }

    primaryStage.setTitle("NGram Analyser")
    primaryStage.setMaximized(true)
    primaryStage.setScene(scene)
    primaryStage.show
  }
}

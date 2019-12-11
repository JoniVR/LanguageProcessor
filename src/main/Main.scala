import Dto.AnalysisDto
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import utilities.IOManager

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
    val loader = new FXMLLoader(getClass.getResource("/view/MainScene.fxml"))
    val mainPane: BorderPane = loader.load()
    val scene: Scene = new Scene(mainPane)

    primaryStage.setTitle("NGram Analyser")
    primaryStage.setMaximized(false)

    primaryStage.setResizable(false)
    primaryStage.setWidth(1280)
    primaryStage.setHeight(800)

    primaryStage.setScene(scene)
    primaryStage.show()
  }
}

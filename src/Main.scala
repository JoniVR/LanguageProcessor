import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import presenter.NGramsPresenter
import view.NGramsView

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

    primaryStage.setTitle("NGram Analyser")
    primaryStage.setMaximized(false)

    primaryStage.setResizable(false)
    primaryStage.setWidth(1280)
    primaryStage.setHeight(800)

    primaryStage.setScene(scene)
    primaryStage.show
  }
}

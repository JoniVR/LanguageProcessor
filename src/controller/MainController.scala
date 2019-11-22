package controller

import javafx.fxml.FXML
import javafx.scene.control.MenuItem
import javafx.stage.{FileChooser, Stage}

class MainController
{
    @FXML private var uploadMenuItem: MenuItem = _
    @FXML private var preferencesMenuItem: MenuItem = _
    @FXML private var aboutMenuItem: MenuItem = _

    @FXML
    def uploadMenuClicked(): Unit = {
        println("upload clicked")

        /*
        // Load fxml and open a new stage
        val loader = new FXMLLoader(getClass.getResource("/view/UploadScene.fxml"))
        val uploadPane: BorderPane = loader.load()
        val scene: Scene = new Scene(uploadPane)
        val stage = new Stage

        stage.setTitle("Upload")
        stage.setResizable(false)
        stage.setScene(scene)
        stage.show()
        */

        val fileChooser = new FileChooser
        val files = fileChooser.showOpenMultipleDialog(new Stage())
        if (!files.isEmpty) {
            files.forEach(f =>
                println(f.getPath)
            )
        } else {
            println("No file selected")
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
}

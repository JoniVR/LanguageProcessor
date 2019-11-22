package controller

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.stage.{FileChooser, Stage}

import scala.reflect.io.File

class UploadController {

	@FXML private var btnUpload: Button = _

	@FXML
	def uploadClicked(): Unit = {
		println("upload clicked")

	}

	@FXML
	def fileSelectClicked(): Unit = {
		println("file select clicked")

		/*
		val fileChooser = new FileChooser
		val files = fileChooser.showOpenMultipleDialog(new Stage())
		files.forEach(f => println(f.getPath))
		*/
	}
}

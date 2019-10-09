package view

import java.net.URL

import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import presenter.NGramsPresenter

class NGramsView(NGramsPresenter: NGramsPresenter) extends GridPane
{
  // Adding css stylesheet, found in the resources folder
  val css : String = this.getClass.getClassLoader.getResource("style/style.css").toExternalForm
  getStylesheets.add(css)

  // Setting width and height
  this.setMaxWidth(800)
  this.setMaxHeight(600)

  val label = new Label("Hello World!")
  label.setId("hello")

  this.getChildren.add(label)
}

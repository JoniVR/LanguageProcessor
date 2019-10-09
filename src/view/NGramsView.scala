package view

import java.net.URL

import javafx.scene.control.{Label, TabPane}
import presenter.NGramsPresenter
import javafx.scene.control.Tab
import javafx.scene.control.TabPane

class NGramsView(NGramsPresenter: NGramsPresenter) extends TabPane
{
  // Adding css stylesheet, found in the resources folder
  val css : String = this.getClass.getClassLoader.getResource("style/style.css").toExternalForm
  getStylesheets.add(css)

  val label1 = new Label("Hello,")
  val label2 = new Label(" World!")
  label1.setId("hello")

  val tab1 = new Tab
  tab1.setText("Hello")
  tab1.setContent(label1)

  val tab2 = new Tab
  tab2.setText("World")
  tab2.setContent(label2)
  //tab.setContent(new Nothing(200, 200, Color.LIGHTSTEELBLUE))
  this.getTabs.add(tab1)
  this.getTabs.add(tab2)
  //this.getChildren.add(label1)
}

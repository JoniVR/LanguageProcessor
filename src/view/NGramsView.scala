package view

import javafx.scene.control.{Label, TabPane}
import presenter.NGramsPresenter
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.BorderPane

class NGramsView(NGramsPresenter: NGramsPresenter) extends BorderPane
{
  // Adding css stylesheet, found in the resources folder
  val css : String = this.getClass.getClassLoader.getResource("style/style.css").toExternalForm
  getStylesheets.add(css)

  // Hello world labels
  val label1 = new Label("Hello,")
  val label2 = new Label(" World!")
  label1.setId("hello")
  label2.setId("hello")

  // Language tabs
  val tab1 = new Tab
  tab1.setText("Hello")
  tab1.setContent(label1)
  tab1.setClosable(false)

  val tab2 = new Tab
  tab2.setText("World")
  tab2.setContent(label2)
  tab2.setClosable(false)

  // Tab pane
  val tabPane = new TabPane
  tabPane.getStyleClass.add("tab-pane")
  tabPane.getTabs.add(tab1)
  tabPane.getTabs.add(tab2)

  this.setCenter(tabPane)
}

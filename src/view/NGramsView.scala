package view

import javafx.scene.control.{Label, Menu, MenuBar, MenuItem, Tab, TabPane}
import presenter.NGramsPresenter
import javafx.scene.layout.BorderPane
import model.Languages

class NGramsView(NGramsPresenter: NGramsPresenter) extends BorderPane {

  /*
   * Adding CSS stylesheet
   */
  val css: String = this.getClass.getClassLoader.getResource("style/style.css").toExternalForm
  getStylesheets.add(css)

  /*
   * Creating nodes and adding them to screen
   */
  val fileMenu = new Menu("File")
  val uploadItem = new MenuItem("Upload")
  val resetItem = new MenuItem("Reset")
  fileMenu.getItems.addAll(uploadItem, resetItem)

  val optionsMenu = new Menu("Options")
  val prefItem = new MenuItem("Preferences")
  optionsMenu.getItems.add(prefItem)

  val helpMenu = new Menu("Help")
  val aboutItem = new MenuItem("About")
  helpMenu.getItems.add(aboutItem)

  val menuBar = new MenuBar
  menuBar.getMenus.addAll(fileMenu, optionsMenu, helpMenu)
  this.setTop(menuBar)

  val tabPane = new TabPane
  tabPane.getStyleClass.add("tab-pane")
  this.setCenter(tabPane)

  /*
   * Create tabs for every language and add to TabPane
   * TODO: Create a custom Pane implementation to add to the tabs
   */
  Languages.values.foreach(lang => {
    val tab = new Tab
    tab.setText(lang.name)
    tab.setClosable(false)

    val lblHello = new Label(s"Hello, ${lang.name}!")
    lblHello.getStyleClass.add("hello")
    tab.setContent(lblHello)

    tabPane.getTabs.add(tab)
  })

  /*
   * Adding event handlers to the nodes
   */
  uploadItem.setOnAction(e => {
    println("Hello from Upload!")
  })

  resetItem.setOnAction(e => {
    println("Hello from Reset!")
  })

  prefItem.setOnAction(e => {
    println("Hello from Preferences!")
  })

  aboutItem.setOnAction(e => {
    println("Hello from About!")
  })
}

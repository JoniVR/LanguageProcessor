package view

import javafx.scene.layout.GridPane
import presenter.NGramsPresenter

class NGramsView(NGramsPresenter: NGramsPresenter) extends GridPane
{
  setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, DarkSlateGray, Black);")
}

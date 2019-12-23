package presenter

import javafx.fxml.FXML
import javafx.scene.control.Label
import model.Analysis

class AnalysisTabPresenter {

  @FXML private var analysisNameLabel: Label = _
  @FXML private var analysisLanguageLabel: Label = _

  def setAnalysis(analysis: Analysis): Unit = {
    if (analysis == null) throw new IllegalArgumentException("Analysis cannot be null")
    analysisNameLabel.setText(analysis.name)
    analysisLanguageLabel.setText(analysis.language)
  }
}

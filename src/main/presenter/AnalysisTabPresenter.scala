package presenter

import java.util.Comparator

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.geometry.Side
import javafx.scene.chart._
import javafx.scene.control.{ComboBox, Label, Tooltip}
import javafx.scene.layout.BorderPane
import model.Analysis

class AnalysisTabPresenter {

  @FXML private var analysisNameLabel: Label = _
  @FXML private var analysisLanguageLabel: Label = _
  @FXML private var analysisTypeComboBox: ComboBox[String] = _
  @FXML private var rootPane: BorderPane = _

  /**
   * Configures the new tab when an analysis is loaded
   * @param analysis ...
   */
  def loadAnalysis(analysis: Analysis): Unit = {
    if (analysis == null) throw new IllegalArgumentException("Analysis cannot be null")
    analysisNameLabel.setText(analysis.name)
    analysisLanguageLabel.setText(analysis.language)
    analysisSelectionSetup(analysis)
  }

  /**
   * Adds event listeners to the combo box so that the user can switch between the different analysis
   */
  private def analysisSelectionSetup(analysis: Analysis): Unit = {
    analysisTypeComboBox.valueProperty.addListener((o: javafx.beans.value.ObservableValue[_ <: String], oldVal: String, newVal: String) => {
      newVal match {
        case "Frequency of words starting with letter in alphabet" =>
          val xAxis = new CategoryAxis
          val yAxis = new NumberAxis
          val chart = new BarChart[String, Number](xAxis, yAxis)
          xAxis.setLabel("Letter")
          yAxis.setLabel("Frequency [0 ≤ x ≤ 1]")
          val series = new XYChart.Series[String, Number]()
          analysis.numberOfWordsStartingWithLetter
            .foreach(el => {
              series.getData.add(new XYChart.Data(el._1, el._2))
            })
          series.getData.sort(Comparator.comparingDouble(data => - data.getYValue.doubleValue()))
          chart.getData.add(series)
          chart.setLegendVisible(false)
          rootPane.setCenter(chart)


        case "Frequency of words ending with letter in alphabet" =>
          val xAxis = new CategoryAxis
          val yAxis = new NumberAxis
          val chart = new BarChart[String, Number](xAxis, yAxis)
          xAxis.setLabel("Letter")
          yAxis.setLabel("Frequency [0 ≤ x ≤ 1]")
          val series = new XYChart.Series[String, Number]()
          analysis.numberOfWordsEndingWithLetter
            .foreach(el => {
              series.getData.add(new XYChart.Data(el._1, el._2))
            })
          series.getData.sort(Comparator.comparingDouble(data => - data.getYValue.doubleValue()))
          chart.getData.add(series)
          chart.setLegendVisible(false)
          rootPane.setCenter(chart)


        case "Frequency of letter in alphabet" =>
          val xAxis = new CategoryAxis
          val yAxis = new NumberAxis
          val chart = new BarChart[String, Number](xAxis, yAxis)
          xAxis.setLabel("Letter")
          yAxis.setLabel("Frequency [0 ≤ x ≤ 1]")
          val series = new XYChart.Series[String, Number]()
          analysis.frequencyOfLetters
            .foreach(el => {
              series.getData.add(new XYChart.Data(el._1, el._2))
            })
          series.getData.sort(Comparator.comparingDouble(data => - data.getYValue.doubleValue()))
          chart.getData.add(series)
          chart.setLegendVisible(false)
          rootPane.setCenter(chart)

        case "Vowels and consonants" =>
          val data = FXCollections.observableArrayList(
            new PieChart.Data("Vowels", analysis.vowelsPercentage),
            new PieChart.Data("Consonants", analysis.consonantsPercentage)
          )
          val chart = new PieChart(data)
          chart.setLegendSide(Side.LEFT)
          chart.getData.forEach(data => {
            val caption = s"${data.getPieValue * 100} %"
            val tooltip = new Tooltip(caption)
            Tooltip.install(data.getNode, tooltip)
          })
          rootPane.setCenter(chart)

        case "Number of words starting with most frequent bigrams" =>
          val xAxis = new CategoryAxis
          val yAxis = new NumberAxis
          val chart = new BarChart[String, Number](xAxis, yAxis)
          xAxis.setLabel("Bigram")
          yAxis.setLabel("Frequency")
          val series = new XYChart.Series[String, Number]()
          analysis.mostFrequentBiGramsStartOfWord
            .foreach(el => {
              series.getData.add(new XYChart.Data(el._1, el._2))
            })
          series.getData.sort(Comparator.comparingInt(data => - data.getYValue.intValue()))
          chart.getData.add(series)
          chart.setLegendVisible(false)
          rootPane.setCenter(chart)

        case "Number of words ending with most frequent bigrams" =>
          val xAxis = new CategoryAxis
          val yAxis = new NumberAxis
          val chart = new BarChart[String, Number](xAxis, yAxis)
          xAxis.setLabel("Bigram")
          yAxis.setLabel("Frequency")
          val series = new XYChart.Series[String, Number]()
          analysis.mostFrequentBiGramsEndOfWord
            .foreach(el => {
              series.getData.add(new XYChart.Data(el._1, el._2))
            })
          series.getData.sort(Comparator.comparingInt(data => - data.getYValue.intValue()))
          chart.getData.add(series)
          chart.setLegendVisible(false)
          rootPane.setCenter(chart)

        case "Frequency of bigrams" =>
          val xAxis = new CategoryAxis
          val yAxis = new NumberAxis
          val chart = new BarChart[String, Number](xAxis, yAxis)
          xAxis.setLabel("Bigram")
          yAxis.setLabel("Frequency [0 ≤ x ≤ 1]")
          val series = new XYChart.Series[String, Number]()
          analysis.mostFrequentBiGrams
            .foreach(el => {
              series.getData.add(new XYChart.Data(el._1, el._2))
            })
          series.getData.sort(Comparator.comparingDouble(data => - data.getYValue.doubleValue()))
          chart.getData.add(series)
          chart.setLegendVisible(false)
          rootPane.setCenter(chart)

        case "Frequency of trigrams" =>
          val xAxis = new CategoryAxis
          val yAxis = new NumberAxis
          val chart = new BarChart[String, Number](xAxis, yAxis)
          xAxis.setLabel("Trigram")
          yAxis.setLabel("Frequency [0 ≤ x ≤ 1]")
          val series = new XYChart.Series[String, Number]()
          analysis.mostFrequentTriGrams
            .foreach(el => {
              series.getData.add(new XYChart.Data(el._1, el._2))
            })
          series.getData.sort(Comparator.comparingDouble(data => - data.getYValue.doubleValue()))
          chart.getData.add(series)
          chart.setLegendVisible(false)
          rootPane.setCenter(chart)

        case "Frequency of skipgrams" =>
          val xAxis = new CategoryAxis
          val yAxis = new NumberAxis
          val chart = new BarChart[String, Number](xAxis, yAxis)
          xAxis.setLabel("Skipgram")
          yAxis.setLabel("Frequency [0 ≤ x ≤ 1]")
          val series = new XYChart.Series[String, Number]()
          analysis.mostFrequentSkipGrams
            .foreach(el => {
              series.getData.add(new XYChart.Data(el._1, el._2))
            })
          series.getData.sort(Comparator.comparingDouble(data => - data.getYValue.doubleValue()))
          chart.getData.add(series)
          chart.setLegendVisible(false)
          rootPane.setCenter(chart)

        case "Corresponding bigram frequency of most frequent skipgrams" =>
          val xAxis = new CategoryAxis
          val yAxis = new NumberAxis
          val chart = new BarChart[String, Number](xAxis, yAxis)
          xAxis.setLabel("Ngram")
          yAxis.setLabel("Frequency [0 ≤ x ≤ 1]")
          val series1 = new XYChart.Series[String, Number]()
          val series2 = new XYChart.Series[String, Number]()
          analysis.mostFrequentSkipGramsMatchingBiGramFrequency._1
            .foreach(el => {
              series1.getData.add(new XYChart.Data(el._1.replace("_", ""), el._2))
            })
          analysis.mostFrequentSkipGramsMatchingBiGramFrequency._2
            .foreach(el => {
              series2.getData.add(new XYChart.Data(el._1, el._2))
            })
          series1.getData.sort(Comparator.comparingDouble(data => - data.getYValue.doubleValue()))
          series1.setName("Skipgrams")
          series2.setName("Bigrams")
          chart.getData.addAll(series1, series2)
          rootPane.setCenter(chart)

        case _ => throw new NotImplementedError
      }
    })
    analysisTypeComboBox.getSelectionModel.selectFirst()
  }
}

package utilities

import model.Analysis
import spray.json._

/**
 * Object that describes the serialization and deserialization of an analysis.
 */
object AnalysisJsonProtocol extends DefaultJsonProtocol {

  private def transformDoubleMapToJsObject(map: Map[String, Double]): JsObject =
    JsObject(map.transform((k, v) => JsNumber(v)))

  private def transformIntMapToJsObject(map: Map[String, Int]): JsObject =
    JsObject(map.transform((k, v) => JsNumber(v)))

  /**
   * Object that does the reading from and writing to Json.
   * @note This object is made implicit so that we can use the .toJson method on an AnalysisDto without
   * needing to specify the correct format, as long as this object is imported in the utilizing class.
   */
  implicit object AnalysisJsonFormat extends RootJsonFormat[Analysis] {

    override def read(json: JsValue): Analysis = {
      val fields = json.asJsObject("Analysis expected.").fields
      new Analysis(
        name =
          fields("name").convertTo[String],
        language =
          fields("language").convertTo[String],
        numberOfWordsStartingWithLetter =
          fields("numberOfWordsStartingWithLetter").convertTo[Map[String, Int]],
        numberOfWordsEndingWithLetter =
          fields("numberOfWordsEndingWithLetter").convertTo[Map[String, Int]],
        frequencyOfLetters =
          fields("frequencyOfLetters").convertTo[Map[String, Double]],
        consonantsPercentage =
          fields("consonantsPercentage").convertTo[Int],
        vowelsPercentage =
          fields("vowelsPercentage").convertTo[Int],
        mostFrequentBiGrams =
          fields("mostFrequentBiGrams").convertTo[Map[String, Double]],
        mostFrequentTriGrams =
          fields("mostFrequentTriGrams").convertTo[Map[String, Double]],
        mostFrequentSkipGrams =
          fields("mostFrequentSkipGrams").convertTo[Map[String, Double]],
        mostFrequentBiGramsStartOfWord =
          fields("mostFrequentBiGramsStartOfWord").convertTo[Map[String, Int]],
        mostFrequentBiGramsEndOfWord =
          fields("mostFrequentBiGramsEndOfWord").convertTo[Map[String, Int]],
        mostFrequentSkipGramsMatchingBiGramFrequency =
          fields("mostFrequentSkipGramsMatchingBiGramFrequency").convertTo[Map[String, Double]]
      )
    }

    override def write(analysis: Analysis): JsValue =
      JsObject(
        Map[String, JsValue](
          "name" -> JsString(analysis.name),
          "language" -> JsString(analysis.language),
          "numberOfWordsStartingWithLetter" -> transformIntMapToJsObject(analysis.numberOfWordsStartingWithLetter),
          "numberOfWordsEndingWithLetter" -> transformIntMapToJsObject(analysis.numberOfWordsEndingWithLetter),
          "frequencyOfLetter" -> transformDoubleMapToJsObject(analysis.frequencyOfLetters),
          "consonantsPercentage" -> JsNumber(analysis.consonantsPercentage),
          "vowelsPercentage" -> JsNumber(analysis.vowelsPercentage),
          "mostFrequentBiGrams" -> transformDoubleMapToJsObject(analysis.mostFrequentBiGrams),
          "mostFrequentTriGrams" -> transformDoubleMapToJsObject(analysis.mostFrequentTriGrams),
          "mostFrequentSkipGrams" -> transformDoubleMapToJsObject(analysis.mostFrequentSkipGrams),
          "mostFrequentBiGramsStartOfWord" -> transformIntMapToJsObject(analysis.mostFrequentBiGramsStartOfWord),
          "mostFrequentBiGramsEndOfWord" -> transformIntMapToJsObject(analysis.mostFrequentBiGramsEndOfWord),
          "mostFrequentSkipGramsMatchingBiGramFrequency" -> transformDoubleMapToJsObject(analysis.mostFrequentSkipGramsMatchingBiGramFrequency),
        )
      )
  }

}

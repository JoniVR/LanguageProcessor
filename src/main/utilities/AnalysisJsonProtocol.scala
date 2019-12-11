package utilities

import Dto.AnalysisDto
import spray.json.{DefaultJsonProtocol, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}


object AnalysisJsonProtocol extends DefaultJsonProtocol {

  /*
  private def transformFreqMapToJsObject(map: Map[String, AnyVal]): JsObject = map match {
    case map: Map[String, Double] => JsObject(
      map.transform((k, v) => JsNumber(v))
    )
    case map: Map[String, Int] => JsObject(
      map.transform((k, v) => JsNumber(v))
    )
    case _ => throw new IllegalArgumentException("Map values need to be either of type Int or Double.")
  }
  */

  /**
   * Transforms a Map[String, Double] to JsObject.
   * @param map is a map of string-keys and double-values.
   * @return JsObject representation of the given map.
   */
  private def transformDoubleMapToJsObject(map: Map[String, Double]): JsObject =
    JsObject( map.transform((k, v) => JsNumber(v)) )

  /**
   * Transforms a Map[String, Int] to JsObject
   * @param map is a map of string-keys and Int-values.
   * @return JsObject representation of the given map.
   */
  private def transformIntMapToJsObject(map: Map[String, Int]): JsObject =
    JsObject( map.transform((k, v) => JsNumber(v)) )

  implicit object AnalysisJsonFormat extends RootJsonFormat[AnalysisDto] {

    //TODO: deserialization of json string
    override def read(json: JsValue): AnalysisDto = ??? //json match {
    //case JsArray(Vector(JsString(name), JsString(language), )) =>
    //  new AnalysisDto(name, language);
    //case _ => DeserializationException("Something went wrong while reading the analysis Json string.")
    //}

    override def write(analysis: AnalysisDto): JsValue = JsObject(
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

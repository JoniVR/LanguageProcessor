package model

import exception.NGramNotPossibleException

import scala.collection.immutable.ListMap

object NGramsAnalyser {

  def getNgrams(filteredVector: Vector[String], n: Int = 2, numberOfElements: Int): Map[String, Int] = {
    if (n <= 1) throw NGramNotPossibleException(s"The n-value $n is not possible.")

    filteredVector.mkString
      .split("\\W+")
      .map(_.toCharArray)
      .flatMap(_
        .sliding(n).toList
        .map(_.mkString)
      ).groupBy(identity)
      .transform((_, v) => v.length)
      .toSeq
      .sortWith(_._2 > _._2) // sort by value
      .to(ListMap)
      .take(numberOfElements)
  }

  def getSkipGrams(filteredVector: Vector[String], numberOfElements: Int): Map[String, Int] = {
    filteredVector.mkString
      .split("\\W+")
      .map(_.toCharArray)
      .flatMap(_
        .sliding(3).toList
        .map(_.mkString)
        .map(_.updated(1, '_'))
      )
      .groupBy(identity)
      .transform((_, v) => v.length)
      .toSeq
      .sortWith(_._2 > _._2) // sort by value
      .to(ListMap)
      .take(numberOfElements)
  }

  /**
   * Gets the frequency (as a percentage) that an ngram occurs relative to the total word count of the vector.
   * @param filteredVector The text passed in as a vector where each value is a line from the text. This should be filtered already to only contain valid chars.
   * @return A double indicating the percentage of occurrence in the text.
   */
  def getNgramFrequency(filteredVector: Vector[String], nGram: String): Double = {
    val totalWordCount: Double = filteredVector.flatMap(_.split("\\W+")).length
    val text = filteredVector.mkString
    val count = nGram.r.findAllIn(text).size
    count/totalWordCount
  }
}

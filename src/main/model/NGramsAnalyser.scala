package model

import exception.NGramNotPossibleException

import scala.collection.immutable.ListMap

object NGramsAnalyser {

  def getNgrams(vector: Vector[String], n: Int = 2, numberOfElements: Int): Map[String, Int] = {
    if (n <= 1) throw NGramNotPossibleException(s"The n-value $n is not possible.")

    vector.mkString
      .toLowerCase.split(' ')
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

  def getSkipGrams(vector: Vector[String], numberOfElements: Int): Map[String, Int] = {
    vector.mkString
      .toLowerCase.split(' ')
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
}

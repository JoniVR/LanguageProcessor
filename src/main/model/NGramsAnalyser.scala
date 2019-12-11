package model

import exception.NGramImpossibleException

class NGramsAnalyser {

  def getNgrams(vector: Vector[String], n: Int = 2): Map[String, Int] = {
    if (n <= 1) throw NGramImpossibleException(s"An ngram with an n-value of $n is not possible.")

    vector.mkString
      .toLowerCase.split(" ")
      .map(_.toCharArray)
      .flatMap(_
        .sliding(n).toList
        .map(_.mkString)
      )
      .groupBy(identity)
      .transform((k, v) => v.size)
  }

  def getSkipGrams(vector: Vector[String]): Map[String, Int] = {
    vector.mkString
      .toLowerCase.split(" ")
      .map(_.toCharArray)
      .flatMap(_
        .sliding(3).toList
        .map(_.mkString)
        .map(_.updated(1, '_'))
      )
      .groupBy(identity)
      .transform((k, v) => v.size)
  }
}

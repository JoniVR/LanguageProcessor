package model

import exception.NGramNotPossibleException

class NGramsAnalyser {
  def getNgrams(vector: Vector[String], n: Int = 2): Map[String, Int] = {
    if (n <= 1) throw NGramNotPossibleException(s"The n-value $n is not possible.")

    vector.mkString
      .toLowerCase
      .split(" ")
      .map(_.toCharArray)
      .flatMap(_
        .sliding(n)
        .toList
        .map(_.mkString)
      )
      .groupBy(identity)
      .transform((k, v) => v.size)
  }
}

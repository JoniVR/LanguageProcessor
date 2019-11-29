package model

import exception.NGramNotPossibleException

class NGramsAnalyser {
  def getNgrams(vector: Vector[String], n: Int = 2): Map[String, Int] = {
    if (n <= 1) throw NGramNotPossibleException(s"The n-value $n is not possible.")

    vector.mkString
      .replaceAll(" ", "").toLowerCase.toCharArray
      .sliding(n).toSeq
      .groupBy(identity)
    //.transform((k, v) => v.size)
  }
}

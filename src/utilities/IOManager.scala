package utilities

class IOManager
{
  @throws(classOf[Exception])
  def readFile(filePath: String): Vector[String] = {
    var bufferedSource:io.Source = null
    try {
      bufferedSource = io.Source.fromFile(filePath)
      val lines = bufferedSource.getLines().toVector
      lines
    } catch {
      case ex: Exception =>
        throw ex
    } finally {
      bufferedSource.close
    }
  }
}

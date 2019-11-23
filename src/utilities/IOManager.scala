package utilities

class IOManager
{
  @throws(classOf[Exception])
  def readFile(filePath: String): Iterator[String] = {
    var bufferedSource:io.Source = null
    try {
      bufferedSource = io.Source.fromFile(filePath)
      val lines = bufferedSource.getLines()
      lines
    } catch {
      case ex: Exception =>
        throw ex
    } finally {
      bufferedSource.close
    }
  }
}

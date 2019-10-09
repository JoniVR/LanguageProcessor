package utilities

import java.io.{FileNotFoundException, IOException}

class IOManager
{
  // Returning optional so that we can return None in case we have an exception.
  def readFile(filePath: String): Option[Iterator[String]] = {
    try {
      val file = io.Source.fromFile(filePath)
      Some(file.getLines())
    } catch {
      case ex: FileNotFoundException =>
        //TODO: handle exception in UI
        None
      case ex: IOException =>
        //TODO: handle exception in UI
        None
    }
  }
}

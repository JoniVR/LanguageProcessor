package presenter;

import utilities.IOManager;
import utilities.Preprocessor;

// NOTE: Kan zijn dat dit samen kan met andere presenter
class PreprocessorPresenter {

    val preprocessor = new Preprocessor()

    // TODO: Rename / respond to event
    def fileUploadClicked() {
        // TODO: Get path from UI and pass to `readFile`
        val ioManager = new IOManager()
        val fileIterator = ioManager.readFile("files/europarl/dutch/Alldata Dutch.txt")
        if (fileIterator.isDefined) {
            // preprocessing spaces, loading processed list in memory (only once!)
            val processedList = fileIterator.get
                    .filter(!preprocessor.findSpaceLines(_))
                    .map(preprocessor.removeSpaces)
                    .to(List)
            // logging part
//            time {
                preprocessor.logWordCount(processedList)
                preprocessor.logPunctuationMarkCount(processedList)
//            }
        }
    }

    // function for testing function execution duration.
    private def time[R](block: => R): R = {
        val t0 = System.nanoTime()
        val result = block    // call-by-name
        val t1 = System.nanoTime()
        println("Elapsed time: " + (t1 - t0) + "ns")
        result
    }
}

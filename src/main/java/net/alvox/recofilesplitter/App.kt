package net.alvox.recofilesplitter

import java.io.File
import java.nio.charset.Charset

fun main(args: Array<String>) {

    if (args.size < 2) {
        println("Usage .: java -jar reco-file-splitter.jar [file name] [number of records]")
        println("Example: java -jar reco-file-splitter.jar my-file.txt 50")
        return
    }

    val fileName = args[0]
    val recordsNumber = Integer.parseInt(args[1])

    println("File ..............................: $fileName")
    println("Number of records in resulted files: $recordsNumber")

    split(fileName, recordsNumber)

    println("Status ............................: DONE")
}

fun split(fileName: String, numberOfRecords: Int) {
    val iterator = File(fileName).readLines(Charset.forName("UTF-8")).iterator()
    val header = iterator.next()
    var recordCounter = 0
    var file = RecoFile(fileName, header)

    while (iterator.hasNext()) {
        if (recordCounter == numberOfRecords) {
            file.saveAndReset()
            recordCounter = 0
            continue
        }
        val line = iterator.next()
        if (line.length == 0) {
            continue
        }
        if (!iterator.hasNext()) {
            file.save()
            return
        }
        file.addRecord(line)
        recordCounter++
    }
}
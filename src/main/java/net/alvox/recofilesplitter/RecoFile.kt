package net.alvox.recofilesplitter

import java.io.File
import java.nio.charset.Charset
import java.util.*

class RecoFile (val originalFileName: String, val header: String) {

    private val separator = "\r\n"
    private val trailer = RecoTrailer()

    private var fileCounter = 1;
    private var records: MutableList<String> = ArrayList()

    fun addRecord(record: String) {
        trailer.updateAmounts(record)
        records.add(record)
    }

    fun save() {
        if (records.isEmpty()) {
            return
        }
        File(composeFileName()).writeBytes(getContent())
    }

    fun saveAndReset() {
        save()
        reset()
    }

    private fun getContent(): ByteArray {
        val builder = StringBuilder(header).append(separator)
        records.forEach {record -> builder.append(record).append(separator)}
        builder.append(trailer.compose()).append(separator)
        return builder.toString().toByteArray(Charset.forName("us-ascii"))
    }

    private fun composeFileName(): String {
        val baseName = originalFileName.split(".").dropLast(1).reduce { s1, s2 -> s1 + "." + s2 }
        val extension =  originalFileName.split(".").last()
        return baseName + "_" + fileCounter + "." + extension
    }

    private fun reset() {
        trailer.reset()
        records.clear()
        fileCounter++
    }

}
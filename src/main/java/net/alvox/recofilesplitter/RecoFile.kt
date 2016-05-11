package net.alvox.recofilesplitter

import java.io.File
import java.nio.charset.Charset
import java.util.*

class RecoFile (val originalFileName: String, val header: String) {

    private val separator = "\r\n"

    private var debitCount: Long   = 0
    private var creditCount: Long  = 0
    private var debitAmount: Long  = 0
    private var creditAmount: Long = 0

    private var fileCounter = 1;

    private var records: MutableList<String> = ArrayList()

    fun addRecord(record: String) {
        if (isDebit(record)) {
            debitCount++
            debitAmount += getAmount(record)
        } else {
            creditCount++
            creditAmount -= getAmount(record)
        }
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
        var builder = StringBuilder(header).append(separator)
        records.forEach {record -> builder.append(record).append(separator)}
        builder.append(createTrailer()).append(separator)
        return builder.toString().toByteArray(Charset.forName("us-ascii"))
    }

    private fun isDebit(record: String): Boolean {
        return getTransactionType(record) == 10
    }

    private fun getTransactionType(record: String): Int {
        val type = record.subSequence(64, 66).toString()
        return Integer.parseInt(type)
    }

    private fun getAmount(record: String): Int {
        val amount = record.subSequence(67, 75).toString()
        return Integer.parseInt(amount)
    }

    private fun createTrailer(): String {
        return TrailerConverter.convert(debitCount, debitAmount, creditCount, creditAmount)
    }

    private fun composeFileName(): String {
        val baseName = originalFileName.split(".").dropLast(1).reduce { s1, s2 -> s1 + "." + s2 }
        val extension =  originalFileName.split(".").last()
        return baseName + "_" + fileCounter + "." + extension
    }

    private fun reset() {
        debitCount = 0
        debitAmount = 0
        creditCount = 0
        creditAmount = 0
        records.clear()
        fileCounter++
    }

}
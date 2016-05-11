package net.alvox.recofilesplitter

class RecoTrailer {

    private var debitCount: Long   = 0
    private var creditCount: Long  = 0
    private var debitAmount: Long  = 0
    private var creditAmount: Long = 0

    private val typeRange = IntRange(64, 65)
    private val amountRange = IntRange(67, 74)

    fun updateAmounts(record: String) {
        if (isDebit(record)) {
            debitCount++
            debitAmount += getInt(record, amountRange)
        } else {
            creditCount++
            creditAmount -= getInt(record, amountRange)
        }
    }

    fun compose(): String {
        return TrailerConverter.convert(debitCount, debitAmount, creditCount, creditAmount)
    }

    fun reset() {
        debitCount = 0
        debitAmount = 0
        creditCount = 0
        creditAmount = 0
    }

    private fun getInt(record: String, range: IntRange): Int {
        val i = record.substring(range)
        return Integer.parseInt(i)
    }

    private fun isDebit(record: String): Boolean {
        return getInt(record, typeRange) == 10
    }

}
package net.alvox.recofilesplitter

class RecoTrailer {

    private var debitCount: Long   = 0
    private var creditCount: Long  = 0
    private var debitAmount: Long  = 0
    private var creditAmount: Long = 0

    fun updateAmounts(record: String) {
        if (isDebit(record)) {
            debitCount++
            debitAmount += getAmount(record)
        } else {
            creditCount++
            creditAmount -= getAmount(record)
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

    private fun getTransactionType(record: String): Int {
        val type = record.subSequence(64, 66).toString()
        return Integer.parseInt(type)
    }

    private fun getAmount(record: String): Int {
        val amount = record.subSequence(67, 75).toString()
        return Integer.parseInt(amount)
    }

    private fun isDebit(record: String): Boolean {
        return getTransactionType(record) == 10
    }

}
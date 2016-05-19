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
        return compose(debitCount, debitAmount, creditCount, creditAmount)
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

    private companion object Composer {
        private val charByDigit = hashMapOf(10 to "{",  11 to "A",  12 to "B",  13 to "C",  14 to "D",
                                            15 to "E",  16 to "F",  17 to "G",  18 to "H",  19 to "I",
                                           -10 to "}", -11 to "J", -12 to "K", -13 to "L", -14 to "M",
                                           -15 to "N", -16 to "O", -17 to "P", -18 to "Q", -19 to "R")

        private val counterPad = 9
        private val amountPad = 15
        private val fillChar = '0'

        fun compose(debitCounter: Long, debitAmount: Long, creditCounter: Long, creditAmount: Long): String {
            val trailer =  "9" +
                    debitCounter.toString().padStart(counterPad, fillChar) +
                    convert(debitAmount).padStart(amountPad, fillChar) +
                    creditCounter.toString().padStart(counterPad, fillChar) +
                    convert(creditAmount).padStart(amountPad, fillChar)
            return trailer.padEnd(165)
        }

        fun convert(value: Long?): String {
            val v = value ?: 0
            val vstr = Math.abs(v).toString();
            val lastDigit = (10 + Character.getNumericValue(vstr.last())) * ssign(v)
            return vstr.dropLast(1) + charByDigit[lastDigit]
        }

        fun ssign(value: Long): Int {
            return if (value < 0) -1 else 1
        }
    }

}
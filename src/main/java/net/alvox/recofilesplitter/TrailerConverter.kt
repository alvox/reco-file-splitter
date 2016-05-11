package net.alvox.recofilesplitter

object TrailerConverter {

    private val charByDigit = hashMapOf(10 to "{",  11 to "A",  12 to "B",  13 to "C",  14 to "D",
                                        15 to "E",  16 to "F",  17 to "G",  18 to "H",  19 to "I",
                                       -10 to "}", -11 to "J", -12 to "K", -13 to "L", -14 to "M",
                                       -15 to "N", -16 to "O", -17 to "P", -18 to "Q", -19 to "R")

    private val counterPad = 9
    private val amountPad = 15
    private val fillChar = '0'

    fun convert(debitCounter: Long, debitAmount: Long, creditCounter: Long, creditAmount: Long): String {
        val trailer =  "9" +
                debitCounter.toString().padStart(counterPad, fillChar) +
                convert(debitAmount).padStart(amountPad, fillChar) +
                creditCounter.toString().padStart(counterPad, fillChar) +
                convert(creditAmount).padStart(amountPad, fillChar)
        return trailer.padEnd(165)
    }

    private fun convert(value: Long?): String {
        val v = value ?: 0
        val vstr = Math.abs(v).toString();
        val lastDigit = (10 + Character.getNumericValue(vstr.last())) * ssign(v)
        return vstr.dropLast(1) + charByDigit[lastDigit]
    }

    private fun ssign(value: Long): Int {
        return if (value < 0) -1 else 1
    }

}
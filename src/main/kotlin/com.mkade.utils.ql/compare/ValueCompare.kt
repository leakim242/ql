package com.mkade.utils.ql.compare

object ValueCompare {

    private val EMPTY = listOf("__empty__", "", "\"\"")
    fun compare(expected: String, actual: String): Boolean {

        if (EMPTY.contains(expected)) {
            return actual == ""
        }

        return if (expected.isContainsCompare()) {
            actual.contains(expected.trim('*'))
        } else if (expected.isPrefixCompare()) {
            actual.startsWith(expected.trim('*'))
        } else if (expected.isSuffixCompare()) {
            actual.endsWith(expected.trim('*'))
        } else if (expected.isRegexCompare()) {
            val regex = expected.trim().substring("regex(".length, expected.length - 1).trim().toRegex()
            actual.matches(regex)
        } else {
            actual == expected
        }
    }

    private fun String.isPrefixCompare() = endsWith("*")
    private fun String.isSuffixCompare() = startsWith("*")
    private fun String.isContainsCompare() = isPrefixCompare() && isSuffixCompare()
    private fun String.isRegexCompare() = trim().startsWith("regex", ignoreCase = true)
}
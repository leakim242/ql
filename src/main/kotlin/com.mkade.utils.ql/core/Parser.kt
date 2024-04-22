package com.mkade.utils.ql.core

import java.lang.Exception
import java.lang.IllegalArgumentException

class Parser {
    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        val AND = Char(185)
        @OptIn(ExperimentalStdlibApi::class)
        val OR = Char(178)
    }

    fun parse(s: String): Expression {
        return parseInternal(s)
    }

    private fun parseInternal(e: String): Or {

        val ors = mutableListOf<Expression>()
        val s2 = e
            .replace(" AND ", AND.toString())
            .replace(" OR ", OR.toString())

        val s = e
            .replace(" AND ", AND)
            .replace(" and ", AND)
            .replace(" OR ", OR)
            .replace(" or ", OR)

        split(s, OR).forEach { or ->
            if (or.isSingle()) {
                ors.add(Single(or.trim()))
            } else {
                val ands = mutableListOf<Expression>()
                split(or, AND).forEach { and ->
                    if (and.hasOperator()) {
                        if (and.isGroup()) {
                            ands.add(Group(parse(and.trim().extract())))
                        } else {
                            ands.add(Group(parse(and.trim())))
                        }
                    } else {
                        ands.add(Single(and.trim()))
                    }
                }
                ors.add(And(ands))
            }
        }
        return Or(ors)
    }

    private fun String.hasOperator(): Boolean =
        contains(AND) || contains(OR)

    private fun String.isSingle(): Boolean =
        !hasOperator()

    private fun String.extract(): String =
        this.trim().run { substring(1, length - 1) }

    private fun String.isGroup(): Boolean =
        this.trim().run { startsWith('(') && endsWith(')') }

    private fun String.validate() {
        this.trim().run {
            if (startsWith('(')) {
                throw Exception("Syntax error, missing )")
            } else if (endsWith(')')) {
                throw Exception("Syntax error, missing (")
            }
        }
    }

    private fun String.replace(replace: String, replacement: Char): String {
        var balance = 0
        val scanLen = replace.length

        if (length < scanLen) {
            return this
        }

        count { it == '\"' }.let { c ->
            if (c != 0 && c % 2 != 0) {
                throw IllegalArgumentException("Expression got unbalanced double quotes (\")")
            }
        }

        var start = 0
        var end = scanLen
        val replaced = StringBuilder()
        loop@ while (end <= length) {

            val sub = substring(startIndex = start, endIndex = end)
            if (sub.startsWith("\"")) {
                balance++
            }

            if (sub == replace && balance == 0) {
                replaced.append(replacement)
                start += scanLen
                end += scanLen
                balance = 0
            } else {
                replaced.append(sub[0])
                start++
                end++
            }

            if (balance == 2) {
                balance = 0
            }
        }
        replaced.append(substring(end - scanLen))

        return replaced.toString()
    }

    private fun split(s: String, delimiter: Char): List<String> {

        val r = mutableListOf<String>()
        val b = StringBuilder()
        var pBalance = 0
        for (c in s) {

            if (c == '(') {
                pBalance++
            }
            if (c == ')') {
                pBalance--
            }
            if (c == delimiter && pBalance == 0) {
                r.add(b.toString().trim())
                b.clear()
            } else {
                b.append(c)
            }
        }
        if (b.isNotEmpty()) {
            r.add(b.toString())
        }

        return r
    }
}

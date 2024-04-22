package com.mlan.utils.query.impl.map

import com.mlan.utils.query.compare.ValueCompare
import com.mlan.utils.query.core.Evaluator
import java.util.Locale

class MapKvEvaluator(private val map: Map<String, Any?>?) : Evaluator {

    init {
        assert(map != null)
    }

    private val internal = cast(map!!)
    private val regexOptions = setOf(RegexOption.IGNORE_CASE)
    private val inReqEx = "(\\s*)IN(\\s*)\\(.*".toRegex(regexOptions)
    private val noInReqEx = "(\\s*)NOT(\\s*)IN(\\s*)\\(.*".toRegex(regexOptions)

    @OptIn(ExperimentalStdlibApi::class)
    override fun evaluate(e: String): Boolean {

        when {
            e.contains(">=") -> return checkGreaterEquals(e)
            e.contains("<=") -> return checkLessEquals(e)
            e.contains("!=") -> return checkNotEquals(e)
            e.contains(">") -> return checkGreater(e)
            e.contains("<") -> return checkLess(e)
            e.contains("=") -> return checkEquals(e)
            e.uppercase(Locale.getDefault()).contains(noInReqEx) -> return checkNotIN(e)
            e.uppercase(Locale.getDefault()).contains(inReqEx) -> return checkIN(e)
        }
        return false
    }

    private fun checkIN(e: String): Boolean {

        val kv = e.split("in", ignoreCase = true).map { it.trim() }
        if (kv.size != 2 && !kv[1].endsWith(")")) {
            throw IllegalMapExpression(e)
        }
        val inValues = kv[1].substring(1, kv[1].length - 1).split(",").map { it.trim() }

        internal[kv.first()]?.let {
            return inValues.any { expected -> ValueCompare.compare(expected, it) }
        }
        return false
    }

    private fun checkNotIN(e: String): Boolean {

        var last = 'X'
        var addAll = false
        val trimmed = StringBuilder()

        e.forEach {

            if (it == '(') {
                addAll = true
            }

            if (addAll) {
                trimmed.append(it)
            } else if (!(last == ' ' && it == ' ')) {
                trimmed.append(it)
            }
            last = it
        }

        val kv = trimmed.split("not in", ignoreCase = true).map { it.trim() }
        if (kv.size != 2 && !kv[1].endsWith(")")) {
            throw IllegalMapExpression(e)
        }
        val inValues = kv[1].substring(1, kv[1].length - 1).split(",").map { it.trim() }

        internal[kv.first()]?.let {
            return inValues.none { expected -> ValueCompare.compare(expected, it) }
        }
        return false
    }

    private fun checkEquals(e: String): Boolean {
        val kv = e.split("=").map { it.trim() }
        if (kv.size != 2) {
            throw IllegalMapExpression(e)
        }
        val expectedValue = kv[1]

        internal[kv.first()]?.let { headerValue ->
            return ValueCompare.compare(expectedValue, headerValue)
        }
        return false
    }

    private fun checkNotEquals(e: String): Boolean {
        val kv = e.split("!=").map { it.trim() }
        if (kv.size != 2) {
            throw IllegalMapExpression(e)
        }
        val expectedValue = kv[1]

        internal[kv.first()]?.let { headerValue ->
            return !ValueCompare.compare(expectedValue, headerValue)
        }
        return false
    }

    private fun checkGreater(e: String): Boolean {
        val kv = e.split(">").map { it.trim() }
        if (kv.size != 2) {
            throw IllegalMapExpression(e)
        }
        internal[kv.first()]?.let {
            return it > kv[1]
        }
        return false
    }

    private fun checkGreaterEquals(e: String): Boolean {
        val kv = e.split(">=").map { it.trim() }
        if (kv.size != 2) {
            throw IllegalMapExpression(e)
        }
        internal[kv.first()]?.let {
            return it >= kv[1]
        }
        return false
    }

    private fun checkLess(e: String): Boolean {
        val kv = e.split("<").map { it.trim() }
        if (kv.size != 2) {
            throw IllegalMapExpression(e)
        }
        internal[kv.first()]?.let {
            return it < kv[1]
        }
        return false
    }

    private fun checkLessEquals(e: String): Boolean {
        val kv = e.split("<=").map { it.trim() }
        if (kv.size != 2) {
            throw IllegalMapExpression(e)
        }
        internal[kv.first()]?.let {
            return it <= kv[1]
        }
        return false
    }

    private fun cast(map: Map<String, Any?>): Map<String, String?> =
        map.map { it.key to it.value as String }.associate { it.first to it.second }


}
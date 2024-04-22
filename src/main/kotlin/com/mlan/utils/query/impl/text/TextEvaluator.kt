package com.mlan.utils.query.impl.text

import com.mlan.utils.query.core.Evaluator

class TextEvaluator(private val source: String) : Evaluator {
    override fun evaluate(e: String): Boolean {
        return source.contains(e)
    }
}

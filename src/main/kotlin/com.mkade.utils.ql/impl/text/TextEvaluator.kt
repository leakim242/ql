package com.mkade.utils.ql.impl.text

import com.mkade.utils.ql.core.Evaluator

class TextEvaluator(private val source: String) : Evaluator {
    override fun evaluate(e: String): Boolean {
        return source.contains(e)
    }
}

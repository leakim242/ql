package com.mlan.utils.query.impl.text

import com.mlan.utils.query.core.Expression
import com.mlan.utils.query.core.ExpressionExecutor
import com.mlan.utils.query.core.Parser

class TextExpressionExecutor : ExpressionExecutor {

    override fun evaluate(source: Any, expression: String): Boolean {

        val exp = Parser().parse(expression)

        if (source !is String) {
            throw IllegalArgumentException("Source must be a String")
        }

        return exp.evaluate(TextEvaluator(source))
    }

    override fun evaluate(source: Any, expression: Expression): Boolean {
        if (source !is String) {
            throw IllegalArgumentException("Source must be a String")
        }

        return expression.evaluate(TextEvaluator(source))
    }
}

package com.mkade.utils.ql.impl.text

import com.mkade.utils.ql.core.Expression
import com.mkade.utils.ql.core.ExpressionExecutor
import com.mkade.utils.ql.core.Parser

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

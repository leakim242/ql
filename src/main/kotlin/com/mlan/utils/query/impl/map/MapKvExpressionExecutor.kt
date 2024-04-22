package com.mlan.utils.query.impl.map

import com.mlan.utils.query.core.Expression
import com.mlan.utils.query.core.ExpressionExecutor
import com.mlan.utils.query.core.Parser

class MapKvExpressionExecutor : ExpressionExecutor {

    override fun evaluate(source: Any, expression: String): Boolean {

        val exp = Parser().parse(expression)

        if (source !is Map<*, *>) {
            throw IllegalArgumentException("Source must be a Map")
        }

        return exp.evaluate(MapKvEvaluator(cast(source)))
    }

    override fun evaluate(source: Any, expression: Expression): Boolean {
        if (source !is Map<*, *>) {
            throw IllegalArgumentException("Source must be a Map")
        }

        return expression.evaluate(MapKvEvaluator(cast(source)))
    }

    private fun cast(source: Map<*, *>): Map<String, Any?> {
        return source.map { it.key as String to it.value }
            .associate { it.first to it.second }
    }
}

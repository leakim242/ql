package com.mkade.utils.ql.impl.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.mkade.utils.ql.core.Expression
import com.mkade.utils.ql.core.ExpressionExecutor
import com.mkade.utils.ql.core.Parser
import java.lang.IllegalArgumentException

class JsonPathExpressionExecutor : ExpressionExecutor {
    private val validatorObjectMapper = ObjectMapper()

    override fun evaluate(source: Any, expression: String): Boolean {

        val exp = Parser().parse(expression)

        if (source !is String) {
            throw IllegalArgumentException("Source must be valid JSON String")
        }
        try {
            source.isValidJson()
        } catch (t: Throwable) {
            throw IllegalArgumentException("Source must be valid JSON String", t)
        }

        return exp.evaluate(JsonPathEvaluator(source))
    }

    override fun evaluate(source: Any, expression: Expression): Boolean {
        if (source !is String) {
            throw IllegalArgumentException("Source must be valid JSON String")
        }
        try {
            source.isValidJson()
        } catch (t: Throwable) {
            throw IllegalArgumentException("Source must be valid JSON String", t)
        }

        return expression.evaluate(JsonPathEvaluator(source))
    }

    private fun String.isValidJson() {
        validatorObjectMapper.readTree(this)
    }
}

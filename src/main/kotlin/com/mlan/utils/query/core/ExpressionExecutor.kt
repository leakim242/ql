package com.mlan.utils.query.core

interface ExpressionExecutor {
    fun evaluate(source: Any, expression: String): Boolean

    fun evaluate(source: Any, expression: Expression): Boolean
}

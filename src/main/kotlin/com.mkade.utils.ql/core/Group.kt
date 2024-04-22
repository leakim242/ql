package com.mkade.utils.ql.core

class Group(private val expression: Expression) : Expression {

    override fun evaluate(evaluator: Evaluator): Boolean {
        return when (expression) {
            is Single -> expression.evaluate(evaluator)
            is Or -> expression.expressions().any { it.evaluate(evaluator) }
            is And -> expression.expressions().all { it.evaluate(evaluator) }
            else -> false
        }
    }

    override fun toString(): String {
        return when (expression) {
            is Single -> "Group($expression)"
            is Or -> "Group(${expression.expressions().joinToString(" OR ")})"
            is And -> "Group(${expression.expressions().joinToString(" AND ")})"
            else -> ""
        }
    }
}

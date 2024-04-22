package com.mkade.utils.ql.core

class Or(private val e: List<Expression>) : Expression {

    fun expressions() = e

    override fun evaluate(evaluator: Evaluator): Boolean =
        e.any { it.evaluate(evaluator) }

    override fun toString(): String =
        "(${e.joinToString(separator = " OR ")})"
}

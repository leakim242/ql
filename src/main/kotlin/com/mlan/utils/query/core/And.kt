package com.mlan.utils.query.core

class And(private val e: List<Expression>) : Expression {

    fun expressions(): List<Expression> = e
    override fun evaluate(evaluator: Evaluator): Boolean =
        e.all { it.evaluate(evaluator) }

    override fun toString(): String =
        "[${e.joinToString(separator = " AND ")}]"
}

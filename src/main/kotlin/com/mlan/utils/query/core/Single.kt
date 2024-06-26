package com.mlan.utils.query.core

class Single(private val e: String) : Expression {
    override fun evaluate(evaluator: Evaluator): Boolean {
        return evaluator.evaluate(e)
    }

    override fun toString(): String = e
}

package com.mlan.utils.query.core

interface Expression {
    fun evaluate(evaluator: Evaluator): Boolean
}

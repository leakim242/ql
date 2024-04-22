package com.mkade.utils.ql.core

interface Expression {
    fun evaluate(evaluator: Evaluator): Boolean
}

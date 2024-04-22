package com.mkade.utils.ql.impl.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.MissingNode
import com.mkade.utils.ql.core.Evaluator

class JsonPathEvaluator(source: String) : Evaluator {
    private val mapper = ObjectMapper()
    private val node = mapper.readTree(source)
    override fun evaluate(e: String): Boolean {

        e.split(" IN").let { splitResult ->
            if (splitResult.size == 2) {
                val path = splitResult[0].trim()
                val expected = splitResult[1]
                    .trim()
                    .trim('(', ')')
                    .split(',')
                    .map { it.trim() }

                val nodes = JsonUtils.findNodesForKey(node, path)
                return expected.any { it == nodes.first().textValue() }
            }
        }

        e.split("=").let {
            if (it.size != 2) {
                throw IllegalJsonPathExpression(e)
            }
            val path = it[0].trim()
            val expected = it[1].trim()

            val nodes = JsonUtils.findNodesForKey(node, path)

            return nodes
                .filterNot { node -> node is MissingNode }
                .map { valueNode -> valueNode.textValue() }
                .map { valueNode ->
                    println(valueNode)
                    valueNode
                }
                .contains(expected)
        }
    }
}

// ARTMAS05.IDOC.E1BPE1MARART.DEL_FLAG=X

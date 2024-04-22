package com.mkade.utils.ql.impl.json

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.MissingNode


object JsonUtils {

    fun findNodesForKey(inputNode: JsonNode, keySelectorPath: String): List<JsonNode> {
        val list = mutableListOf<JsonNode>()
        findNodesForKey(inputNode, keySelectorPath, list)
        if (list.isEmpty()) {
            list.add(MissingNode.getInstance())
        }
        return list
    }

    private fun findNodesForKey(inputNode: JsonNode, keySelectorPath: String, list: MutableList<JsonNode>) {
        val path = keySelectorPath.split(".")
        var node = inputNode
        var skip = false
        for (p in path) {
            if (!skip) {
                if (node.isArray) {
                    (node as ArrayNode).forEach { entry ->
                        if (!entry.isValueNode) {
                            findNodesForKey(
                                entry,
                                path.filterUntil(p).joinToString("."),
                                list
                            )
                        } else {
                            list.add(entry)
                        }
                    }
                    // Since now all entries are visited.
                    skip = true
                } else {
                    node = node.findPath(p)
                }
            }
        }
        if (node.isValueNode) {
            list.add(node)
        } else if (node.isArray && !skip) {
            (node as ArrayNode).forEach { entry ->
                if (entry.isValueNode) {
                    list.add(entry)
                } else if (entry.isObject) {
                    list.add(entry)
                }
            }
        } else if (!node.isMissingNode && !skip) {
            list.add(node)
        }
    }

    private fun List<String>.filterUntil(s: String): List<String> {
        val list = mutableListOf<String>()
        var add = false
        forEach {
            if (s == it) {
                add = true
            }
            if (add) {
                list.add(it)
            }
        }
        return list
    }
}

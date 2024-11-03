package org.example.detekt.report

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

fun getActiveRules(yamlContent: String): Map<String, List<String>> {
    val mapper = ObjectMapper(YAMLFactory())
    val rootNode = mapper.readTree(yamlContent)
    return collectActiveRules(rootNode, mutableMapOf(), null)
}

fun collectActiveRules(
    node: JsonNode,
    activeRules: MutableMap<String, List<String>>,
    ruleSetId: String? = null,
): MutableMap<String, List<String>> {
    node.fields().forEach { (key, value) ->
        if (value.isObject) {
            val isActive = value.get("active")?.asBoolean() ?: false
            when {
                ruleSetId == null && isActive -> {
                    activeRules[key] = emptyList()
                }

                ruleSetId != null && isActive -> {
                    activeRules[ruleSetId] = activeRules[ruleSetId].orEmpty().toMutableList().apply {
                        add(key)
                    }
                }
            }
            collectActiveRules(value, activeRules, key)
        }
    }
    return activeRules
}

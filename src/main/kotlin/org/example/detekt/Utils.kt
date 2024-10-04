package org.example.detekt

import org.example.detekt.arch.ModuleType
import org.jetbrains.kotlin.psi.KtNamedFunction

fun getCurrentLayer(path: String): ModuleType? =
    ModuleType.entries.findLast { type ->
        val pathBlocks = path.split("/")
        pathBlocks.minus(pathBlocks.last())
            .any { blockOfPath ->
                type.possibleNames.any { blockOfPath.contains(it, ignoreCase = true) }
            }
    }


fun isCommon(path: String): Boolean {
    val pathBlocks = path.split("/")
    return pathBlocks.minus(pathBlocks.last())
        .any { blockOfPath ->
            listOf("core", "common", "app").any { blockOfPath.contains(it, ignoreCase = true) }
        }
}

val KtNamedFunction.body get() = this.bodyExpression ?: this.bodyExpression

package org.lucpc.interactions

import org.lucpc.interactions.commands.PostForumProblems
import org.lucpc.interactions.commands.PostInformation
import org.lucpc.interactions.commands.PostRules

class InteractionCollection {
    val interactions: List<Interaction<*>> = listOf(
        PostForumProblems(),
        PostInformation(),
        PostRules()
    )

    fun <T> getInteractionByName(arg: String): Interaction<T> {
        return interactions.filterIsInstance<Interaction<T>>().first { it.name == arg }
    }
}
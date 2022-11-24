package org.lucpc.interactions

import net.dv8tion.jda.api.interactions.commands.build.CommandData

interface Interaction<T> {
    fun executeCommand(event: T)
    val commandData: CommandData

    val interactionType: InteractionType
        get() = InteractionType.GUILD

    val admin : Boolean

    val name: String
        get() = commandData.name

    enum class InteractionType {
        GUILD,
        PUBLIC
    }
}
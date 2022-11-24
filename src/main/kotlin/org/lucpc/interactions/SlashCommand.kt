package org.lucpc.interactions

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands

/**
 * This class is the super class of all commands
 * It sets the name, description, channel type, and other aliases you may want
 *
 * The class is abstract for use of the abstract method executeCommand
 */
abstract class SlashCommand(
    name: String,
    val description: String,
    override val admin: Boolean = false,
    override val interactionType: Interaction.InteractionType = Interaction.InteractionType.GUILD
) : Interaction<SlashCommandInteractionEvent> {

    abstract override fun executeCommand(event: SlashCommandInteractionEvent)

    override val commandData = Commands.slash(name, description)
}

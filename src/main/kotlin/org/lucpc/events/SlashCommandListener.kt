package org.lucpc.events

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.lucpc.Leetle
import org.lucpc.interactions.Interaction
import org.lucpc.utils.sendEmbedReply

class SlashCommandListener : ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val command = Leetle.interactionCollection.getInteractionByName<SlashCommandInteractionEvent>(event.name)

        if(command.interactionType == Interaction.InteractionType.PUBLIC && event.channelType != ChannelType.PRIVATE){
            event.sendEmbedReply(true) {
                title = "Error"
                description = "Wrong channel for this (dm me the command)"
                error = true
            }
            return
        }

        if (command.admin && !event.guild!!.getMember(event.user)!!.hasPermission(Permission.ADMINISTRATOR)){
            event.sendEmbedReply(true) {
                title = "Error"
                description = "You have insufficient permissions for this command"
                error = true
            }
            return
        }


        command.executeCommand(event)
    }

}
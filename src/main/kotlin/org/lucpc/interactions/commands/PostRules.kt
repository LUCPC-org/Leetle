package org.lucpc.interactions.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.lucpc.interactions.SlashCommand
import org.lucpc.utils.buildEmbed
import org.lucpc.utils.sendEmbedReply

class PostRules : SlashCommand("post-rules", "This will post an embed of all the rules", admin = true) {

    override fun executeCommand(event: SlashCommandInteractionEvent) {
        event.channel.sendMessageEmbeds(buildEmbed {
            title = "Rules"
            description =
                    "**1.** Follow Discord TOS and the Liberty Way\n" +
                    "**2.** Don't spam.\n" +
                    "**3.** Think before you speak .\n" +
                    "**4.** Don't evade any type of ban or mute.\n" +
                    "**5.** Botting is not permitted under any circumstances. \n"

        }.build()).queue()

        event.sendEmbedReply(true) {
            title = "Success"
        }
    }
}
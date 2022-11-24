package org.lucpc.interactions.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.lucpc.interactions.SlashCommand
import org.lucpc.utils.buildEmbed
import org.lucpc.utils.sendEmbedReply

class PostInformation : SlashCommand("post-info", "Posts information about LUCPC", admin = true) {

    override fun executeCommand(event: SlashCommandInteractionEvent) {
        event.channel.sendMessageEmbeds(buildEmbed {
            title = "Links"
            fields = mapOf(
                "Website" to "https://lucpc.org",
                "Github" to "https://github.com/LUCPC-org"
            )
        }.build()).queue()

        event.channel.sendMessageEmbeds(buildEmbed {
            title = "Information"
            fields = mapOf(
                "Purpose" to "Develop the skills necessary to solve programming problems for technical interviews and competition, while also providing tools and opportunity to compete against peers.",
                "Meetings" to "Thursdays: 5-6:00 PM\nSchool of Business: Room 2830",
                "Weekly Competitions" to "Three problems are provided each week to solve outside of the club meetings. The leaderboard ranks current completion of the problems by the members of the club."
            )
        }.build()).queue()


        event.sendEmbedReply(true) {
            title = "Success"
        }
    }
}
package org.lucpc.interactions.commands

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import net.dv8tion.jda.api.entities.channel.forums.ForumTagSnowflake
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import org.jsoup.Jsoup
import org.lucpc.Leetle
import org.lucpc.interactions.SlashCommand
import org.lucpc.utils.*
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*


class PostForumProblems :
    SlashCommand(
        "post-problems", "This command will post 3 forum posts for the 3 problems that week",
        admin = true
    ) {

    val gson = GsonBuilder().setPrettyPrinting().create()

    override fun executeCommand(event: SlashCommandInteractionEvent) {
        val startOfWeek = getStartOfWeek()
        //This is the url for the problems api
        val url = "https://lucpc-edf35-default-rtdb.firebaseio.com/problems/${startOfWeek}.json"
        val content = ConnectionUtil.httpsConnection(url)!!.content
        //Convert the contents to JsonObject
        val jsonObject = gson.fromJson(content, JsonObject::class.java)

        val easyURL = jsonObject.getAsJsonObject("easy").get("url").asString
        val mediumURL = jsonObject.getAsJsonObject("medium").get("url").asString
        val hardURL = jsonObject.getAsJsonObject("hard").get("url").asString

        val problemNames = arrayOf(
            easyURL.dropLast(1).substringAfterLast('/'),
            mediumURL.dropLast(1).substringAfterLast('/'),
            hardURL.dropLast(1).substringAfterLast('/')
        )

        val problems = mutableListOf<Problem>()

        for ((count, name) in problemNames.withIndex()) {
            val problemURL = "https://leetcode.com/problems/${name}"
            val document = Jsoup.connect(problemURL).get()
            val elements = document.head().getElementsByAttributeValueContaining("name", "description")
            if (elements.size < 1) {
                event.sendEmbedReply(true) {
                    title = "Error"
                    description = "No elements found matching \'name\': description"
                    error = true
                }
                return
            }

            val problemContent = elements.attr("content")


            val problemName = name.split("-").map { it.replaceFirstChar(Char::titlecase) }.joinToString(" ")


            problems.add(
                Problem(
                    problemName, problemContent,
                    "https://leetcode.com/problems/${name}", Difficulty.values()[count]
                )
            )
        }

        val forum = event.guild!!.getForumChannelById(Leetle.FORUM_CHANNEL_ID)!!


        for (problem in problems) {
            val postName = "${problem.name} ($startOfWeek)"
            forum.createForumPost(postName, MessageCreateBuilder()
                .addContent("${problem.link}\n")

                .addEmbeds(buildEmbed {
                    title = "**Description**"
                    description = problem.description
                }.build())

                .build()
            ).setTags(
                forum.getAvailableTagsByName(problem.difficulty.tagName, true)
            ).queue()
        }

        event.sendEmbedReply {
            title = "Success"
        }
    }

    private val TZ = ZoneId.of("UTC-5")
    fun getStartOfWeek(): String {
        return LocalDate.now(TZ).with(TemporalAdjusters.previousOrSame(WeekFields.of(Locale.US).getFirstDayOfWeek()))
            .toString()
    }

}
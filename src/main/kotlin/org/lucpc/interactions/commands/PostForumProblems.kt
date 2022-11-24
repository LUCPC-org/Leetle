package org.lucpc.interactions.commands

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import net.dv8tion.jda.api.entities.channel.forums.ForumTagSnowflake
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
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
        for (name in problemNames) {
            val apiUrl = "https://leet-api-code.herokuapp.com/api/v1/${name}"
            val apiContent = ConnectionUtil.httpsConnection(apiUrl)!!.content
            val apiObject = gson.fromJson(apiContent, JsonObject::class.java).getAsJsonArray("message")

            val problemName = apiObject[0].asJsonObject.get("name").asString
            //We have to do "-1" because it starts out at 1
            val difficulty = Difficulty.values()[apiObject[0].asJsonObject.get("difficulty").asInt - 1]
            val problemDescription = apiObject[1].asJsonObject.get("content").asString

            problems.add(
                Problem(
                    problemName, problemDescription,
                    "https://leetcode.com/problems/${name}", difficulty
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
package org.lucpc

import net.dv8tion.jda.api.JDABuilder
import org.lucpc.events.SlashCommandListener
import org.lucpc.interactions.InteractionCollection
import java.awt.Color
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object Leetle {

    lateinit var token: String
    const val GUILD_ID = "620730579712999444"
    const val FORUM_CHANNEL_ID = "1039377359037542490"

    val ERROR_COLOR = Color(255, 50, 100)
    val NORMAL_COLOR = Color(255, 255, 255)

    const val DEFAULT_FOOTER = "made with ❤️ by wzid | lucpc.org"

    lateinit var interactionCollection : InteractionCollection

    fun loadToken(){
        val inputStream = this::class.java.getResourceAsStream("/bot.token")!!
        token = BufferedReader(InputStreamReader(inputStream)).readLine()
    }

    fun initialize(builder: JDABuilder) {
        interactionCollection = InteractionCollection()
        builder.addEventListeners(SlashCommandListener())
    }

}
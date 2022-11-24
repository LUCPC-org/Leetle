package org.lucpc.utils

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback
import org.lucpc.Leetle

data class Embed(
    var title: String? = null,
    var url: String? = null,
    var description: String? = null,
    var error: Boolean = false,
    var author: String? = null,
    var authorURL: String? = null,
    var imageURL: String? = null,
    var fields: Map<String, String>? = null
)

fun IReplyCallback.sendEmbedReply(ephemeral: Boolean = false, inlineFields : Boolean = false, init: Embed.() -> Unit) {
    val embed = Embed().apply { init() }
    val embedBuilder = createEmbed {
        with(embed) {
            setTitle(title, url)
            setDescription(description)
            setColor(if (error) Leetle.ERROR_COLOR else Leetle.NORMAL_COLOR)
            setAuthor(author, authorURL, authorURL)
            setImage(imageURL)
            fields ?: return@with
            for ((fieldName, fieldDescription) in fields!!) {
                addField(fieldName, fieldDescription, inlineFields)
            }
        }
    }
    this.replyEmbeds(embedBuilder.build()).setEphemeral(ephemeral).queue()
}

fun buildEmbed(init: Embed.() -> Unit): EmbedBuilder {
    val embed = Embed().apply { init() }
    val embedBuilder = createEmbed {
        with(embed) {
            setTitle(title, url)
            setDescription(description)
            setColor(if (error) Leetle.ERROR_COLOR else Leetle.NORMAL_COLOR)
            setAuthor(author, authorURL, authorURL)
            setImage(imageURL)
            fields ?: return@with
            fields!!.forEach { addField(it.key, it.value, false)}
        }
    }
    return embedBuilder
}

fun createEmbed(style: EmbedBuilder.() -> Unit): EmbedBuilder {
    val embed = EmbedBuilder().setFooter(Leetle.DEFAULT_FOOTER)
    embed.style()
    return embed
}

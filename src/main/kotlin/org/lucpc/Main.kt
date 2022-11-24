import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.lucpc.Leetle

fun main() {
    Leetle.loadToken()

    val builder = JDABuilder.createDefault(Leetle.token)
        .setChunkingFilter(ChunkingFilter.ALL)
        .setMemberCachePolicy(MemberCachePolicy.ALL)
        .enableIntents(GatewayIntent.GUILD_MEMBERS)

    Leetle.initialize(builder)

    builder.addEventListeners(object : ListenerAdapter() {
        override fun onReady(event: ReadyEvent) {
            val jda = event.jda
            jda.presence.activity = Activity.streaming("Weekly Problems", "https://lucpc.org/problems")

            val guild = jda.getGuildById(Leetle.GUILD_ID)!!

            guild.updateCommands().addCommands(Leetle.interactionCollection.interactions.map { it.commandData }).queue()
        }
    })

    builder.build()

}
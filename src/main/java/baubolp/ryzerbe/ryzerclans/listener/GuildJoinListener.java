package baubolp.ryzerbe.ryzerclans.listener;

import baubolp.ryzerbe.ryzerclans.RyZerClans;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.Instant;

public class GuildJoinListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        String id = event.getGuild().getId();
        RyZerClans.getDatabaseManager().registerClan(id);

        for (TextChannel textChannel : event.getGuild().getTextChannels()) {
            if (textChannel.canTalk()) {
                try {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setTitle("Danke, dass ihr mich eingeladen habt.");
                    embedBuilder.setAuthor("RyZerBE");
                    embedBuilder.setColor(Color.GRAY);
                    embedBuilder.setTimestamp(Instant.now());
                    embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
                    embedBuilder.setDescription("Danke, dass ihr mich eingeladen habt. Ich bin der ClanWar Clanbot von RyZerBE. Ihr könnt meine Commands mit " + RyZerClans.prefix + "help aufrufen. Solltet ihr Bugs entdecken, dann meldet die Bugs doch auf unserem Discord.\n [Klicke um beizutreten](https://discord.ryzer.be/)");
                    embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
                    textChannel.sendMessage(embedBuilder.build()).queue();
                    break;
                }catch (Exception ignored){}
            }
        }
    }
}

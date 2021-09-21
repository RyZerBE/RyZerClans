package baubolp.ryzerbe.ryzerclans.listener;

import baubolp.ryzerbe.ryzerclans.RyZerClans;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.Instant;

public class GuildTextChannelDeleteListener extends ListenerAdapter {

    @Override
    public void onTextChannelDelete(@Nonnull TextChannelDeleteEvent event) {
        String guildId = event.getGuild().getId();
        if (RyZerClans.getDatabaseManager().validClanDiscord(guildId)) {
            if (!event.getChannel().getId().equals(RyZerClans.getDatabaseManager().getClanWarInformationChannel(guildId)))
                return;

            for (TextChannel textChannel : event.getGuild().getTextChannels()) {
                if (!textChannel.canTalk()) continue;
                try {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setAuthor("ClanWar Channel");
                    embedBuilder.setTitle(RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(event.getGuild().getId()), "clanwar-channel-deleted"));
                    embedBuilder.setColor(Color.GRAY);
                    embedBuilder.setTimestamp(Instant.now());
                    embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
                    embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");

                    textChannel.sendMessage(embedBuilder.build()).queue();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            RyZerClans.getDatabaseManager().resetClanWarChannel(guildId);
        }
    }
}

package baubolp.ryzerbe.ryzerclans.command.defaults;

import baubolp.ryzerbe.ryzerclans.RyZerClans;
import baubolp.ryzerbe.ryzerclans.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.Instant;

public class TopTenCommand extends Command {

    public TopTenCommand() {
        super("top10");
    }

    @Override
    public void execute(Member sender, TextChannel channel, String[] args, Message label) {
        if(RyZerClans.getCooldown().hasCooldown(sender.getId())) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(label.getGuild().getId()), "stop-command-spamming"));
            embedBuilder.setAuthor("RyZerBE");
            embedBuilder.setTimestamp(Instant.now());
            embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
            embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        RyZerClans.getCooldown().add(sender.getId());
        if(!channel.canTalk()) return;

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("RyZerBE");
        embedBuilder.setTitle("TOP 10 BY ELO");
        embedBuilder.setDescription(RyZerClans.getDatabaseManager().fetchTop10Clans());
        embedBuilder.setTimestamp(Instant.now());
        embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
        embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
        channel.sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public String getDescription(Guild guild) {
        return RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(guild.getId()), "help-top-ten");
    }
}

package baubolp.ryzerbe.ryzerclans.command.defaults;

import baubolp.ryzerbe.ryzerclans.RyZerClans;
import baubolp.ryzerbe.ryzerclans.command.Command;
import baubolp.ryzerbe.ryzerclans.language.Language;
import baubolp.ryzerbe.ryzerclans.mysql.DatabaseManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.Instant;

public class ClanWarChannelCommand extends Command {

    public ClanWarChannelCommand() {
        super("setchannel");
    }

    @Override
    public void execute(Member sender, TextChannel channel, String[] args, Message label) {
        if(!(sender.hasPermission(Permission.ADMINISTRATOR))) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("Error");
            embedBuilder.setTitle(RyZerClans.getLanguageProvider().getTranslation(Language.GERMAN, "no-perms"));
            embedBuilder.setColor(Color.RED);
            embedBuilder.setTimestamp(Instant.now());
            embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
            embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

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
        if(args.length < 2 || label.getMentionedChannels().size() == 0 || label.getMentionedChannels().size() > 1) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("Error");
            embedBuilder.setTitle(RyZerClans.prefix + "setchannel <#Channel>");
            embedBuilder.setColor(Color.RED);
            embedBuilder.setTimestamp(Instant.now());
            embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
            embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        TextChannel textChannel = label.getMentionedChannels().get(0);
        String id = textChannel.getId();
        DatabaseManager databaseManager = RyZerClans.getDatabaseManager();
        databaseManager.setClanWarChannel(label.getGuild().getId(), id);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Successfully");
        embedBuilder.setTitle(RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(label.getGuild().getId()), "clanwar-channel-set"));
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setTimestamp(Instant.now());
        embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
        embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
        channel.sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public String getDescription(Guild guild) {
        return RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(guild.getId()), "help-cw-channel");
    }
}

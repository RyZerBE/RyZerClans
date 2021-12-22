package baubolp.ryzerbe.ryzerclans.command.defaults;

import baubolp.ryzerbe.ryzerclans.RyZerClans;
import baubolp.ryzerbe.ryzerclans.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.Instant;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(Member sender, TextChannel channel, String[] args, Message label) {
        if(!channel.canTalk()) return;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Help");
        embedBuilder.setTitle(RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(label.getGuild().getId()), "list-of-commands"));
        embedBuilder.setColor(Color.GRAY);
        for(Command command : RyZerClans.getCommandMap().getCommands()) {
            if(command.getName().equals("updatelog")) continue;
            embedBuilder.addField(command.getDescription(label.getGuild()), RyZerClans.prefix + command.getName(), false);
        }
        embedBuilder.setTimestamp(Instant.now());
        embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
        embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
        channel.sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public String getDescription(Guild guild) {
        return RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(guild.getId()), "help-help");
    }
}

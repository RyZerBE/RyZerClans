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

public class UpdatelogCommand extends Command {

    public UpdatelogCommand() {
        super("updatelog");
    }

    @Override
    public void execute(Member sender, TextChannel channel, String[] args, Message label) {
        if(!sender.getUser().getId().equals("394479414051864576")) return;
        if(args.length < 2) return;
        if(!channel.canTalk()) return;


        StringBuilder builder = new StringBuilder();
        for(String message : args){
            if(message.equals(RyZerClans.prefix + getName())) continue;
            if(builder.length() == 0) {
                builder.append("• ").append(message);
                continue;
            }
            if(message.equals("||")) {
                builder.append("\n• ");
                continue;
            }

            builder.append(" ").append(message);
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Update");
        embedBuilder.setTitle("ClanWar Update");
        embedBuilder.setColor(Color.GRAY);
        embedBuilder.setTimestamp(Instant.now());
        embedBuilder.setDescription(builder.toString());
        embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
        embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
        for(TextChannel textChannel : RyZerClans.getDatabaseManager().getAllClanWarChannels()) {
            textChannel.sendMessage(embedBuilder.build()).queue();
        }
        label.addReaction("✅").queue();
    }

    @Override
    public String getDescription(Guild guild) {
        return RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(guild.getId()), "help-updatelog");
    }
}

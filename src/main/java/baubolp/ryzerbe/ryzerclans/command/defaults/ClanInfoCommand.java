package baubolp.ryzerbe.ryzerclans.command.defaults;

import baubolp.ryzerbe.ryzerclans.RyZerClans;
import baubolp.ryzerbe.ryzerclans.command.Command;
import baubolp.ryzerbe.ryzerclans.util.ClanInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.Instant;
import java.util.Map;

public class ClanInfoCommand extends Command {

    public ClanInfoCommand() {
        super("claninfo");
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

        if(args.length < 2) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("Error");
            embedBuilder.setTitle(RyZerClans.prefix + "claninfo <ClanName>");
            embedBuilder.setColor(Color.RED);
            embedBuilder.setTimestamp(Instant.now());
            embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
            embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        RyZerClans.getCooldown().add(sender.getId());
        ClanInfo clanInfo = RyZerClans.getDatabaseManager().fetchClanInfo(args[1]);
        if(clanInfo == null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(label.getGuild().getId()), "clan-not-found"));
            embedBuilder.setAuthor("RyZerBE");
            embedBuilder.setTimestamp(Instant.now());
            embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
            embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }

        String state;
        switch (clanInfo.getState()) {
            case 0:
                state = "CLOSED";
                break;
            case 1:
                state = "ONLY INVITE";
                break;
            case 2:
                state = "OPEN FOR EVERYONE";
                break;
            default:
                state = "???";
                break;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(clanInfo.getClanName() + " [" + clanInfo.getClanTag() + "]");
        embedBuilder.setColor(Color.GRAY);

        embedBuilder.setTimestamp(Instant.now());
        String clanInfoString = "__**Created:**__ " + clanInfo.getCreatedDate() +
                "\n__**Elo:**__ " + clanInfo.getElo() +
                "\n__**Rank:**__ " + clanInfo.getRank() +
                "\n__**State:**__ " + state +
                "\n" + clanInfo.getDescription();

        StringBuilder members = new StringBuilder();
        StringBuilder mods = new StringBuilder();
        StringBuilder leader = new StringBuilder();

        clanInfo.getMembers().forEach((playerName, roleName) -> {
            switch (roleName) {
                case "Member":
                    members.append("➼ ").append(playerName).append("\n");
                    break;
                case "Moderator":
                    mods.append("➼ ").append(playerName).append("\n");
                    break;
                case "Leader":
                    leader.append("➼ ").append(playerName).append("\n");
                    break;
            }
        });
        embedBuilder.setDescription(clanInfoString);
        embedBuilder.addField("__**Leader**__", leader.toString(), true);
        if(mods.length() == 0) {
            embedBuilder.addField("__**Moderator**__", "/", true);
        }else {
            embedBuilder.addField("__**Moderator**__", mods.toString(), true);
        }
        if(leader.length() == 0) {
            embedBuilder.addField("__**Members(" + clanInfo.getMembers().size() +")**__", "/", false);
        }else {
            embedBuilder.addField("__**Members(" + clanInfo.getMembers().size() +")**__", members.toString(), false);
        }
        embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
        embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
        channel.sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public String getDescription(Guild guild) {
        return RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(guild.getId()), "help-clan-info");
    }
}

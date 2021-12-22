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

public class ClanNotifyCommand extends Command {

    public ClanNotifyCommand() {
        super("notify");
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
        if(!channel.canTalk()) return;

        if(args.length < 3) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("Error");
            embedBuilder.setTitle(RyZerClans.prefix + "notify <add|remove> <ClanName>");
            embedBuilder.setColor(Color.RED);
            embedBuilder.setTimestamp(Instant.now());
            embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
            embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
            channel.sendMessage(embedBuilder.build()).queue();
            return;
        }
        String clan = args[2];
        DatabaseManager databaseManager = RyZerClans.getDatabaseManager();
        String guildID = channel.getGuild().getId();
        if(args[1].equals("add")) {
            if(databaseManager.isClanNotified(guildID, "*")) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setAuthor("Error");
                embedBuilder.setTitle(RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(guildID), "first-remove-all-before-add"));
                embedBuilder.setColor(Color.RED);
                embedBuilder.setTimestamp(Instant.now());
                embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
                embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
                channel.sendMessage(embedBuilder.build()).queue();
                return;
            }

            if(clan.equals("*")) {
                if(databaseManager.getClanNotifies(guildID).size() > 0) {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setAuthor("Error");
                    embedBuilder.setTitle(RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(guildID), "first-remove-all-before-add"));
                    embedBuilder.setColor(Color.RED);
                    embedBuilder.setTimestamp(Instant.now());
                    embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
                    embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
                    channel.sendMessage(embedBuilder.build()).queue();
                    return;
                }

                databaseManager.addClanNotify(guildID, "*");
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setAuthor("Successfully");
                embedBuilder.setTitle(RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(label.getGuild().getId()), "global-notifications-activated"));
                embedBuilder.setTimestamp(Instant.now());
                embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
                embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
                channel.sendMessage(embedBuilder.build()).queue();
            }else {
                if(!databaseManager.existClan(clan)) {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setAuthor("Error");
                    embedBuilder.setTitle(RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(label.getGuild().getId()), "clan-not-exist"));
                    embedBuilder.setTimestamp(Instant.now());
                    embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
                    embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
                    channel.sendMessage(embedBuilder.build()).queue();
                    return;
                }

                if(databaseManager.isClanNotified(guildID, clan)) {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setAuthor("Error");
                    embedBuilder.setTitle(RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(guildID), "clan-already-added"));
                    embedBuilder.setTimestamp(Instant.now());
                    embedBuilder.setColor(Color.RED);
                    embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
                    embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
                    channel.sendMessage(embedBuilder.build()).queue();
                    return;
                }

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setAuthor("Successfully");
                embedBuilder.setTitle(RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(guildID), "successfully-clan-added"));
                embedBuilder.setTimestamp(Instant.now());
                embedBuilder.setColor(Color.GREEN);
                embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
                embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
                channel.sendMessage(embedBuilder.build()).queue();
                databaseManager.addClanNotify(guildID, clan);
            }
        }else if(args[1].equals("remove")) {
            if(clan.equals("*")) {
                databaseManager.removeAllClanNotifies(guildID);
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setAuthor("Successfully");
                embedBuilder.setTitle(RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(guildID), "successfully-removed-all-clans"));
                embedBuilder.setColor(Color.GREEN);
                embedBuilder.setTimestamp(Instant.now());
                embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
                embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
                channel.sendMessage(embedBuilder.build()).queue();
                return;
            }

            if(databaseManager.isClanNotified(guildID, clan)) {
                databaseManager.removeClanNotify(guildID, clan);
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setAuthor("Successfully");
                embedBuilder.setTitle(RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(guildID), "successfully-removed-clan"));
                embedBuilder.setColor(Color.GREEN);
                embedBuilder.setTimestamp(Instant.now());
                embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
                embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
                channel.sendMessage(embedBuilder.build()).queue();
            }else {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setAuthor("Error");
                embedBuilder.setTitle(RyZerClans.getLanguageProvider().getTranslation(Language.GERMAN, "clan-is-not-added"));
                embedBuilder.setColor(Color.GREEN);
                embedBuilder.setTimestamp(Instant.now());
                embedBuilder.setThumbnail("https://media.discordapp.net/attachments/693494109842833469/731231356092284969/RYZER_png.png?width=703&height=703");
                embedBuilder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
                channel.sendMessage(embedBuilder.build()).queue();
            }
        }
    }

    @Override
    public String getDescription(Guild guild) {
        return RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(guild.getId()), "help-clan-notify");
    }
}

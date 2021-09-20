package baubolp.cloudbridge.bot.Packets;

import baubolp.ryzerbe.ryzerclans.RyZerClans;
import baubolp.ryzerbe.ryzerclans.mysql.DatabaseManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.awt.*;
import java.time.Instant;
import java.util.HashMap;

public class ClanWarResultPacket extends DataPacket {
    public String packetName;

    public ClanWarResultPacket() { packetName = "ClanWarResultPacket"; }

    @Override
    public String encode() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("password", this.getPassword());
        hashMap.put("packetName", this.packetName);
        String encode = JSONValue.toJSONString(hashMap);
        return encode;
    }

    @Override
    public void handle(JSONObject jsonObject) {
        String lineUp1 = jsonObject.get("lineUp1").toString();
        String lineUp2 = jsonObject.get("lineUp2").toString();
        String playTime = jsonObject.get("playTime").toString();
        String arenaName = jsonObject.get("arenaName").toString();
        String winner = jsonObject.get("winner").toString();
        String loser = jsonObject.get("loser").toString();
        int elo = Integer.parseInt(jsonObject.get("elo").toString());

        DatabaseManager databaseManager = RyZerClans.getDatabaseManager();
        for (TextChannel channel : databaseManager.getAllClanWarChannels()) {
            if (databaseManager.isClanNotified(channel.getGuild().getId(), winner) || databaseManager.isClanNotified(channel.getGuild().getId(), loser)) {
                EmbedBuilder embedBuilder = getClanWarSolutionEmbed(winner, loser, String.valueOf(elo), arenaName, playTime, String.join(", ", lineUp1.split(":")), String.join(", ", lineUp2.split(":")));
                channel.sendMessage(embedBuilder.build()).queue();
            }
        }
    }

    private EmbedBuilder getClanWarSolutionEmbed(String winner, String loser, String elo, String map, String duration, String lineup1, String lineup2) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.LIGHT_GRAY);
        builder.setAuthor("ClanWar Result");
        builder.setFooter("RyZerBE", "https://media.discordapp.net/attachments/693494109842833469/730816117311930429/RYZER_Network.png?width=703&height=703");
        builder.setTitle(winner + " vs " + loser);
        builder.setTimestamp(Instant.now());
        builder.addField(":first_place: Winner", winner + " (+ "+ elo +" Elo)", false);
        builder.addField(":second_place: Loser", loser + " (- "+ elo +" Elo)", true);
        builder.addField(":family_man_boy_boy: Lineup of " + winner, lineup1, false);
        builder.addField(":family_man_boy_boy: Lineup of " + loser, lineup2, false);
        builder.addField(":park: Map", map, true);
        builder.addField(":alarm_clock: Duration", duration, true);
        return builder;
    }
}

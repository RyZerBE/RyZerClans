package baubolp.ryzerbe.ryzerclans.mysql;

import baubolp.ryzerbe.ryzerclans.RyZerClans;
import baubolp.ryzerbe.ryzerclans.language.Language;
import baubolp.ryzerbe.ryzerclans.language.LanguageProvider;
import baubolp.ryzerbe.ryzerclans.util.ClanInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseManager {

    public void createTables() {
        Statement statement = RyZerClans.getConnection().getStatement();
        try {
            statement.execute("CREATE TABLE IF NOT EXISTS Clans(id INTEGER NOT NULL KEY AUTO_INCREMENT, guild TEXT, cwchannel TEXT, language varchar(32))");
            statement.execute("CREATE TABLE IF NOT EXISTS CWNotify(id INTEGER NOT NULL KEY AUTO_INCREMENT, guild TEXT, clan_name TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setClanWarChannel(String guild, String textChannelId) {
        if (validClanDiscord(guild)) {
            Statement statement = RyZerClans.getConnection().getStatement();
            try {
                statement.execute("UPDATE `Clans` SET cwchannel='" + textChannelId + "' WHERE guild='" + guild + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void resetClanWarChannel(String guild) {
        setClanWarChannel(guild, "");
    }

    public boolean validClanDiscord(String guild) {
        Statement statement = RyZerClans.getConnection().getStatement();
        try {
            ResultSet result = statement.executeQuery("SELECT guild FROM Clans WHERE guild='" + guild + "'");
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void registerClan(String guild) {
        if (!validClanDiscord(guild)) {
            LanguageProvider.clanLanguage.put(guild, Language.GERMAN);
            Statement statement = RyZerClans.getConnection().getStatement();
            try {
                statement.execute("INSERT INTO `Clans`(`guild`, `cwchannel`, `language`) VALUES ('" + guild + "', '', '" + Language.GERMAN + "')");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void unregisterClan(String guild) {
        if (validClanDiscord(guild)) {
            Statement statement = RyZerClans.getConnection().getStatement();
            try {
                statement.execute("DELETE FROM Clans WHERE guild='" + guild + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setClanWarInformationChannel(String guild, String textchannelid) {
        if (validClanDiscord(guild)) {
            Statement statement = RyZerClans.getConnection().getStatement();
            try {
                statement.execute("UPDATE `Clans` SET cwchannel='" + textchannelid + "' WHERE guild='" + guild + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String getClanWarInformationChannel(String guild) {
        if (validClanDiscord(guild)) {
            Statement statement = RyZerClans.getConnection().getStatement();
            try {
                ResultSet result = statement.executeQuery("SELECT cwchannel FROM Clans WHERE guild='" + guild + "'");
                if (result.next()) return result.getString("cwchannel");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "INVALID";
    }

    public List<String> getClanNotifies(String guild) {
        if(!validClanDiscord(guild)) return new ArrayList<>();

        Statement statement = RyZerClans.getConnection().getStatement();
        List<String > clans = new ArrayList<>();
        try {
            ResultSet notifiedClans = statement.executeQuery("SELECT clan_name FROM CWNotify WHERE guild='" + guild + "'");
            if (notifiedClans.next()) {
                clans.add(notifiedClans.getString("clan_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clans;
    }

    public boolean isClanNotified(String guild, String clanName) {
        if(!validClanDiscord(guild)) return false;

        List<String > clans = new ArrayList<>();
        Statement statement = RyZerClans.getConnection().getStatement();
        try {
            ResultSet notifiedClans = statement.executeQuery("SELECT clan_name FROM CWNotify WHERE guild='" + guild + "'");
            if (notifiedClans.next()) {
                if(notifiedClans.getString("clan_name").equals("*")) return true;
                clans.add(notifiedClans.getString("clan_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clans.contains(clanName);
    }

    public void addClanNotify(String guild, String clan) {
        if(!validClanDiscord(guild)) return;

        Statement statement = RyZerClans.getConnection().getStatement();
        try {
            statement.execute("INSERT INTO `CWNotify`(`guild`, `clan_name`) VALUES ('" + guild + "','" + clan + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeClanNotify(String guild, String clan) {
        if(!validClanDiscord(guild)) return;

        Statement statement = RyZerClans.getConnection().getStatement();
        try {
            statement.execute("DELETE FROM `CWNotify` WHERE guild='" + guild + "' AND clan_name='" + clan + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeAllClanNotifies(String guild) {
        if(!validClanDiscord(guild)) return;

        Statement statement = RyZerClans.getConnection().getStatement();
        try {
            statement.execute("DELETE FROM `CWNotify` WHERE guild='" + guild + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existClan(String clanName) {
        Statement statement = RyZerClans.getClanConnection().getStatement();
        try {
            ResultSet result = statement.executeQuery("SELECT clan_name FROM Clans WHERE clan_name='" + clanName + "'");
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setLanguage(String guild, Language language) {
        Statement statement = RyZerClans.getConnection().getStatement();
        try {
            statement.execute("UPDATE Clans SET language='" + language + "' WHERE guild='" + guild + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TextChannel> getAllClanWarChannels() {
        Statement statement = RyZerClans.getConnection().getStatement();
        ArrayList<TextChannel> channels = new ArrayList<TextChannel>();
        try {
            ResultSet result = statement.executeQuery("SELECT cwchannel, guild FROM Clans");
            while (result.next()) {
                if (result.getString("cwchannel").equals("")) continue;

                String guildId = result.getString("guild");
                String textChannelId = result.getString("cwchannel");
                Guild guild = RyZerClans.getJda().getGuildById(guildId);
                if (guild == null) continue;
                TextChannel channel = guild.getTextChannelById(textChannelId);
                if (channel == null) continue;
                channels.add(channel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return channels;
    }

    public ClanInfo fetchClanInfo(String clanName) {
        Statement statement = RyZerClans.getClanConnection().getStatement();
        try {
            ResultSet result = statement.executeQuery("SELECT * FROM Clans WHERE clan_name='" + clanName + "'");
            if(result.next()) {
                String clanTag = result.getString("clan_tag");
                String created = result.getString("created");
                String description = result.getString("message");
                int elo = result.getInt("elo");
                int stateId = result.getInt("status");
                HashMap<String, String> members = new HashMap<>();
                ResultSet memberResult = statement.executeQuery("SELECT * FROM ClanUsers WHERE clan_name='" + clanName + "'");
                while(memberResult.next()) {
                    String playerName = memberResult.getString("playername");
                    String role = memberResult.getString("role");
                    members.put(playerName, role);
                }

                int rank = 1;
                ResultSet rankResult = statement.executeQuery("SELECT clan_name, elo FROM Clans ORDER BY elo DESC");
                while(rankResult.next()) {
                    if(rankResult.getString("clan_name").equals(clanName)) break;
                    rank++;
                }

                return new ClanInfo(clanName, clanTag, created, members, description, elo, rank, stateId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String fetchTop10Clans() {
        Statement statement = RyZerClans.getClanConnection().getStatement();
        StringBuilder builder = new StringBuilder();
        try {
            ResultSet result = statement.executeQuery("SELECT clan_name, elo FROM Clans ORDER BY elo DESC LIMIT 10");

            int place = 0;
            while (result.next()) {
                String clan = result.getString("clan_name");
                String elo = result.getString("elo");
                builder.append(++place).append(". ").append(clan).append("\n **Elo:** ").append(elo).append("\n");
            }
            return builder.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}

package baubolp.ryzerbe.ryzerclans.mysql;

import baubolp.ryzerbe.ryzerclans.RyZerClans;
import baubolp.ryzerbe.ryzerclans.language.Language;
import baubolp.ryzerbe.ryzerclans.language.LanguageProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    public void createTables() {
        Statement statement = RyZerClans.getConnection().getStatement();
        try {
            statement.execute("CREATE TABLE IF NOT EXISTS Clans(id INTEGER NOT NULL KEY AUTO_INCREMENT, guild TEXT, cwchannel TEXT, language int(2))");
            statement.execute("CREATE TABLE IF NOT EXISTS CWNotify(id INTEGER NOT NULL KEY AUTO_INCREMENT, guild TEXT, clan_name TEXT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                statement.execute("INSERT INTO `Clans`(`guild`, `cwchannel`, `clans`, `language`) VALUES ('" + guild + "', '', '', '" + Language.GERMAN + "')");
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
}

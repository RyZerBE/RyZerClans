package baubolp.ryzerbe.ryzerclans;

import baubolp.cloudbridge.bot.CloudBridge;
import baubolp.ryzerbe.ryzerclans.command.CommandMap;
import baubolp.ryzerbe.ryzerclans.command.defaults.*;
import baubolp.ryzerbe.ryzerclans.language.Language;
import baubolp.ryzerbe.ryzerclans.language.LanguageProvider;
import baubolp.ryzerbe.ryzerclans.listener.CommandListener;
import baubolp.ryzerbe.ryzerclans.listener.GuildJoinListener;
import baubolp.ryzerbe.ryzerclans.listener.GuildLeaveListener;
import baubolp.ryzerbe.ryzerclans.listener.GuildTextChannelDeleteListener;
import baubolp.ryzerbe.ryzerclans.mysql.DatabaseConnection;
import baubolp.ryzerbe.ryzerclans.mysql.DatabaseManager;
import baubolp.ryzerbe.ryzerclans.util.CommandCooldown;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RyZerClans {

    private static JDA jda;
    public static String prefix = ";";

    private static DatabaseConnection connection;
    private static DatabaseConnection clanConnection;
    private static CommandMap commandMap;
    private static LanguageProvider languageProvider;
    private static DatabaseManager databaseManager;
    private static CloudBridge cloudBridge;
    private static final CommandCooldown cooldown = new CommandCooldown(4000);

    public static void main(String[] args) {
        cloudBridge = new CloudBridge();
        commandMap = new CommandMap();
        languageProvider = new LanguageProvider();
        databaseManager = new DatabaseManager();
        connection = new DatabaseConnection("localhost/RyZerClans?serverTimezone=UTC", "baubo", "A]BaAbZ}$Us}s[mw");
        clanConnection = new DatabaseConnection("localhost/BetterClans?serverTimezone=UTC", "baubo", "A]BaAbZ}$Us}s[mw");

        boot();
    }

    private static void boot() {
        JDABuilder jdaBuilder = JDABuilder.createDefault("NzQ5MzgwMTUxNDM1MDY3NTgz.X0rIpg.8_-GBu9l9SeEx5Cd7DdO24AhFOs");
        jdaBuilder.setActivity(Activity.playing("with Clans of RyZerBE"));
        jdaBuilder.setAutoReconnect(true);
        jdaBuilder.setStatus(OnlineStatus.ONLINE);
        getCommandMap().registerCommands(
                new ClanNotifyCommand(),
                new ClanInfoCommand(),
                new ClanWarChannelCommand(),
                new DiscordCommand(),
                new HelpCommand(),
                new LanguageCommand(),
                new TopTenCommand(),
                new UpdatelogCommand()
        );

        jdaBuilder.addEventListeners(
                new CommandListener(),
                new GuildJoinListener(),
                new GuildLeaveListener(),
                new GuildTextChannelDeleteListener()
        );

        try {
            jda = jdaBuilder.build();
            jda.awaitReady();
        } catch (LoginException e) {
            System.err.println("Discord ist nicht ereichbar: " + e.getMessage());
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        try {
            ResultSet rset = connection.getStatement().executeQuery("SELECT * FROM Clans");

            while(rset.next()) {
                Language language;
                if(rset.getString("language").equals("GERMAN")) {
                    language = Language.GERMAN;
                }else {
                    language = Language.ENGLISH;
                }
                LanguageProvider.clanLanguage.put(rset.getString("guild"), language);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            cloudBridge.start("RyZerClans");
        } catch (Exception e) {
            e.printStackTrace();
        }

        getDatabaseManager().createTables();
        getDatabaseManager().initClanDiscords();
        languageProvider.load(Language.GERMAN);
        languageProvider.load(Language.ENGLISH);
    }

    public static CloudBridge getCloudBridge() {
        return cloudBridge;
    }

    public static DatabaseConnection getConnection() {
        return connection;
    }

    public static DatabaseConnection getClanConnection() {
        return clanConnection;
    }

    public static JDA getJda() {
        return jda;
    }

    public static CommandMap getCommandMap() {
        return commandMap;
    }

    public static CommandCooldown getCooldown() {
        return cooldown;
    }

    public static LanguageProvider getLanguageProvider() {
        return languageProvider;
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}

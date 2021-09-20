package baubolp.ryzerbe.ryzerclans.language;

import baubolp.ryzerbe.ryzerclans.RyZerClans;

import java.util.HashMap;

public class LanguageProvider  {

    private static HashMap<String, String> de_translations;
    private static HashMap<String, String> en_translations;
    public static HashMap<String, Language> clanLanguage;

    public LanguageProvider() {
        de_translations = new HashMap<>();
        en_translations = new HashMap<>();
        clanLanguage = new HashMap<>();
    }

    public void load(Language language) {
        if(language == Language.GERMAN) {
            de_translations.put("stop-command-spamming", "Mache nach jedem Command eine kleine Pause..");
            de_translations.put("no-perms", "Du hast keine Rechte auf diesen Command!");
            de_translations.put("first-remove-all-before-add", "Bitte entferne erst alle Clans mit " + RyZerClans.prefix + "removeclan <Clan>");
            de_translations.put("global-notifications-activated", "Du hast die globale Benarechtigung aktiviert.");
            de_translations.put("clan-not-exist", "Dieser Clan existiert nicht.");
            de_translations.put("successfully-clan-added", "Du hast den Clan erfolgreich hinzugefügt.");
            de_translations.put("clan-already-added", "Du hast den Clan bereits hinzugefügt.");
            de_translations.put("list-of-commands", "Liste meiner Commands");
            de_translations.put("help-cw-channel", "Stelle deinen ClanWar Channel ein. Dort erscheinen die Matchergebnisse!");
            de_translations.put("help-top-ten", "Liste der TOP 10 ClanWar Clans");
            de_translations.put("help-clan-notify", "Stelle ein, für welche Clans Matchergebnisse im ClanWar Channel erscheinen sollen.");
            de_translations.put("help-discord", "Bugs? Feedback? Der offizielle RyZerBE Discord");
            de_translations.put("successfully-removed-all-clans", "Du hast erfolgreich alle Clans entfernt.");
            de_translations.put("successfully-removed-clan", "Der Clan wurde erfolgreich entfernt.");
            de_translations.put("clanwar-channel-set", "Du hast erfolgreich den ClanWar Channel gesetzt.");
            de_translations.put("help-language", "Stelle die Sprache des Bots für deinen Discord ein!");
            de_translations.put("clanwar-channel-deleted", "Da der ClanWar Channel gelöscht wurde, erhaltet ihr keine Benarechtigungen mehr.");
            de_translations.put("no-matches-running", "Aktuell spielt kein Clan ein Match.");
            de_translations.put("help-matches", "Alle laufenden ClanWar Matches");
        }else if(language == Language.ENGLISH) {
            en_translations.put("stop-command-spamming", "Do a small break after sending of commands..");
            en_translations.put("no-perms", "You have no permissions to use this command!");
            en_translations.put("help-language", "Change language?");
            //todo: translations
        }
    }

    public String getTranslation(Language language, String key) {
        if(language == Language.GERMAN) {
            if(de_translations.get(key) == null) return key;

            return de_translations.get(key);
        }else if(language == Language.ENGLISH) {
            if(en_translations.get(key) == null) return key;

            return en_translations.get(key);
        }
        return key;
    }

    public Language getClanLangauage(String guild) {
        return clanLanguage.get(guild);
    }
}

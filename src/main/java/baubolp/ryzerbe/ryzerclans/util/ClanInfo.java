package baubolp.ryzerbe.ryzerclans.util;

import java.util.HashMap;

public class ClanInfo {

    private final String clanName;
    private final String clanTag;
    private final String createdDate;
    private final HashMap<String, String> members;
    private final int elo;
    private final int rank;
    private final String description;
    private final int state;

    public ClanInfo(String clanName, String clanTag, String createdDate, HashMap<String, String> members, String description, int elo, int rank, int state) {
        this.elo = elo;
        this.members = members;
        this.description = description;
        this.rank = rank;
        this.state = state;
        this.clanName = clanName;
        this.clanTag = clanTag;
        this.createdDate = createdDate;
    }

    public int getState() {
        return state;
    }

    public int getElo() {
        return elo;
    }

    public HashMap<String, String> getMembers() {
        return members;
    }

    public int getRank() {
        return rank;
    }

    public String getDescription() {
        return description;
    }

    public String getClanName() {
        return clanName;
    }

    public String getClanTag() {
        return clanTag;
    }

    public String getCreatedDate() {
        return createdDate;
    }
}

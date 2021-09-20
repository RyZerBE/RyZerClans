package baubolp.ryzerbe.ryzerclans.listener;

import baubolp.ryzerbe.ryzerclans.RyZerClans;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class GuildLeaveListener extends ListenerAdapter {

    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
        String id = event.getGuild().getId();
        RyZerClans.getDatabaseManager().unregisterClan(id);
    }
}

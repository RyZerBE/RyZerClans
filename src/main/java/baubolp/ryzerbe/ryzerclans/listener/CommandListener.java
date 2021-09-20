package baubolp.ryzerbe.ryzerclans.listener;

import baubolp.ryzerbe.ryzerclans.RyZerClans;
import baubolp.ryzerbe.ryzerclans.command.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;

        for(Command command : RyZerClans.getCommandMap().getCommands()) {
            if(event.getMessage().getContentRaw().startsWith(RyZerClans.prefix + command.getName())) {
                command.execute(event.getMember(), event.getChannel(), event.getMessage().getContentRaw().split(" "), event.getMessage());
                if(command.getName().equals("language")) return;
                event.getMessage().delete().queue();
                break;
            }
        }
    }
}

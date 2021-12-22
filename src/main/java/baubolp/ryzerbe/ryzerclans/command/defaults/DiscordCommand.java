package baubolp.ryzerbe.ryzerclans.command.defaults;

import baubolp.ryzerbe.ryzerclans.RyZerClans;
import baubolp.ryzerbe.ryzerclans.command.Command;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class DiscordCommand extends Command {

    public DiscordCommand() {
        super("discord");
    }

    @Override
    public void execute(Member sender, TextChannel channel, String[] args, Message label) {
        if(!channel.canTalk()) return;
        channel.sendMessage("**Discord:** https://discord.ryzer.be/").queue();
    }

    @Override
    public String getDescription(Guild guild) {
        return RyZerClans.getLanguageProvider().getTranslation(RyZerClans.getLanguageProvider().getClanLangauage(guild.getId()), "help-discord");
    }
}

package baubolp.ryzerbe.ryzerclans.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class Command {

    public String name;

    public Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void execute(Member sender, TextChannel channel, String[] args, Message label) { }

    public String getDescription(Guild guild) {
        return "";
    }
}

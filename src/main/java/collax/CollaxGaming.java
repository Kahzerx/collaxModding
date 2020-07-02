package collax;

import collax.discord.DiscordCommand;
import collax.discord.RandomTpCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

public class CollaxGaming {
    public static String[] bannedWords = {"c", "s", "!!skull", "!!tp", "!!comandos", "!!comandos2", "!!comandos3", "!!resethome"};
    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher){
        DiscordCommand.register(dispatcher);
        RandomTpCommand.register(dispatcher);
    }
}

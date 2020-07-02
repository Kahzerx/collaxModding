package collax;

import collax.here.HereCommand;
import collax.randomTp.RandomTpCommand;
import collax.discord.DiscordCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

public class CollaxGaming {
    public static String[] bannedWords = {"c", "s", "!!skull", "!!tp", "!!comandos", "!!comandos2", "!!comandos3", "!!resethome"};
    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher){
        DiscordCommand.register(dispatcher);
        RandomTpCommand.register(dispatcher);
        HereCommand.register(dispatcher);
    }
}

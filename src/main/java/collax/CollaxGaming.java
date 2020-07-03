package collax;

import collax.here.HereCommand;
import collax.randomTp.RandomTpCommand;
import collax.discord.DiscordCommand;
import collax.where.WhereCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

public class CollaxGaming {
    public static String[] bannedWords = {"c", "C", "s", "S", "!!skull", "!!tp", "!!comandos", "!!comandos2", "!!comandos3", "!!resethome"};
    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher){
        DiscordCommand.register(dispatcher);
        RandomTpCommand.register(dispatcher);
        HereCommand.register(dispatcher);
        WhereCommand.register(dispatcher);
    }
}

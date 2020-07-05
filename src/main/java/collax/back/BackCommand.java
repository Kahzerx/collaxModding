package collax.back;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class BackCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(literal("back").
                executes(context -> tpDeathPos(context.getSource())));
    }
    private static int tpDeathPos(ServerCommandSource source) throws CommandSyntaxException {
        BackFileManager.tpDeathPos(source.getPlayer());
        return 1;
    }
}

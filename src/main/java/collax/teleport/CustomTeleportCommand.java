package collax.teleport;

import collax.CollaxGaming;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandSource.suggestMatching;

public class CustomTeleportCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(literal("tpa").
                then(CommandManager.argument("player", StringArgumentType.word()).
                        suggests((c, b) -> suggestMatching(CollaxGaming.getPlayers(c.getSource()), b)).
                        executes(context -> tp(context.getSource(), StringArgumentType.getString(context, "player")))).
                then(literal("accept").
                        then(CommandManager.argument("player2", StringArgumentType.word()).
                                suggests((c, b) -> suggestMatching(CollaxGaming.getPlayers(c.getSource()), b)).
                                executes(context -> accept(context.getSource(), StringArgumentType.getString(context, "player2"))))).
                then(literal("deny").
                        then(CommandManager.argument("player3", StringArgumentType.word()).
                                suggests((c, b) -> suggestMatching(CollaxGaming.getPlayers(c.getSource()), b)).
                                executes(context -> deny(context.getSource(), StringArgumentType.getString(context, "player3"))))));
    }

    private static int tp(ServerCommandSource source, String player) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = source.getMinecraftServer().getPlayerManager().getPlayer(player);
        if (playerEntity instanceof ServerPlayerEntity){

            if (playerEntity.isSpectator()) {

                if (Integer.parseInt(CollaxGaming.permsArray.get(source.getPlayer().getName().getString())) > 2){

                    Teleport.addPlayer(source.getPlayer().getName().getString(), player);
                    playerEntity.sendMessage(Teleport.getMsg(source.getPlayer().getName().getString()), false);
                }
                else {
                    source.sendFeedback(new LiteralText("no puedes hacerte tp a una persona en modo espectador"), false);
                }
            }
            else{
                Teleport.addPlayer(source.getPlayer().getName().getString(), player);
                playerEntity.sendMessage(Teleport.getMsg(source.getPlayer().getName().getString()), false);
            }
        }
        else source.sendFeedback(new LiteralText("This player doesn't exist"), false);
        return 1;
    }

    public static int accept(ServerCommandSource source, String player) throws CommandSyntaxException {
        Teleport.acceptPlayer(source.getPlayer().getName().getString(), player, source.getMinecraftServer());
        return 1;
    }

    public static int deny(ServerCommandSource source, String player) throws CommandSyntaxException {
        Teleport.denyPlayer(source.getPlayer().getName().getString(), player, source.getMinecraftServer());
        return 1;
    }
}
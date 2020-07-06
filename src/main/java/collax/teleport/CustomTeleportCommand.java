package collax.teleport;

import collax.CollaxGaming;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.world.GameMode;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandSource.suggestMatching;

public class CustomTeleportCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(literal("tp").
                then(CommandManager.argument("player", StringArgumentType.word()).
                        suggests((c, b) -> suggestMatching(CollaxGaming.getPlayers(c.getSource()), b)).
                        executes(context -> tp(context.getSource(), StringArgumentType.getString(context, "player")))));
    }

    private static int tp(ServerCommandSource source, String player) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = source.getMinecraftServer().getPlayerManager().getPlayer(player);
        if (playerEntity instanceof ServerPlayerEntity){
            if (Integer.parseInt(CollaxGaming.permsArray.get(source.getPlayer().getName().getString())) > 1){
                if (playerEntity.isSpectator()){
                    source.getPlayer().setGameMode(GameMode.SPECTATOR);
                    source.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 999999, 0, false, false));
                    source.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 999999, 0, false, false));
                    source.getPlayer().teleport(playerEntity.getServerWorld(), playerEntity.getPos().x, playerEntity.getPos().y, playerEntity.getPos().z, source.getPlayer().yaw, source.getPlayer().pitch);
                    source.sendFeedback(new LiteralText("Remember to use '/s' to go back to survival mode"), false);
                }
                else {
                    source.getPlayer().teleport(playerEntity.getServerWorld(), playerEntity.getPos().x, playerEntity.getPos().y, playerEntity.getPos().z, source.getPlayer().yaw, source.getPlayer().pitch);
                }
            }
            else source.sendFeedback(new LiteralText("You are not allowed to use this command :P"), false);
        }
        else source.sendFeedback(new LiteralText("This player doesn't exist"), false);
        return 1;
    }
}
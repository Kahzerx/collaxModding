package collax.teleport;

import collax.CollaxGaming;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.BlockPosArgumentType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandSource.suggestMatching;

public class AdminTp {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(literal("adminTp").
                requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2)).
                then(CommandManager.argument("player", StringArgumentType.word()).
                        suggests((c, b) -> suggestMatching(CollaxGaming.getPlayers(c.getSource()), b)).
                        executes(context -> tp(context.getSource(), StringArgumentType.getString(context, "player"))).
                        then(CommandManager.argument("player2", StringArgumentType.word()).
                                suggests((c, b) -> suggestMatching(CollaxGaming.getPlayers(c.getSource()), b)).
                                executes(context -> tp(context.getSource(), StringArgumentType.getString(context, "player"), StringArgumentType.getString(context, "player2")))).
                        then(argument("pos", BlockPosArgumentType.blockPos()).
                                executes(context -> tp(context.getSource(), StringArgumentType.getString(context, "player"), BlockPosArgumentType.getBlockPos(context, "pos"))))).
                then(argument("coords", BlockPosArgumentType.blockPos()).
                        executes(context -> tp(context.getSource(), BlockPosArgumentType.getBlockPos(context, "coords")))));
    }

    private static int tp(ServerCommandSource source, BlockPos pos) throws CommandSyntaxException {
        source.getPlayer().teleport(source.getWorld(), pos.getX(), pos.getY(), pos.getZ(), source.getPlayer().yaw, source.getPlayer().pitch);
        return 1;
    }

    private static int tp(ServerCommandSource source, String player, BlockPos pos) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = source.getMinecraftServer().getPlayerManager().getPlayer(player);
        if (playerEntity instanceof ServerPlayerEntity){
            playerEntity.teleport(source.getWorld(), pos.getX(), pos.getY(), pos.getZ(), playerEntity.yaw, playerEntity.pitch);
        }
        else source.sendFeedback(new LiteralText("This player doesn't exist"), false);
        return 1;
    }

    private static int tp(ServerCommandSource source, String player, String player2) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = source.getMinecraftServer().getPlayerManager().getPlayer(player);
        ServerPlayerEntity playerEntity2 = source.getMinecraftServer().getPlayerManager().getPlayer(player2);
        if (playerEntity instanceof ServerPlayerEntity && playerEntity2 instanceof ServerPlayerEntity){
            if (playerEntity2.isSpectator()){
                source.getPlayer().setGameMode(GameMode.SPECTATOR);
                source.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 999999, 0, false, false));
                source.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 999999, 0, false, false));
            }
            playerEntity.teleport(playerEntity2.getServerWorld(), playerEntity2.getPos().x, playerEntity2.getPos().y, playerEntity2.getPos().z, playerEntity.yaw, playerEntity.pitch);
        }
        else source.sendFeedback(new LiteralText("This player doesn't exist"), false);
        return 1;
    }

    private static int tp(ServerCommandSource source, String player) throws CommandSyntaxException {
        ServerPlayerEntity playerEntity = source.getMinecraftServer().getPlayerManager().getPlayer(player);
        if (playerEntity instanceof ServerPlayerEntity){
            if (Integer.parseInt(CollaxGaming.permsArray.get(source.getPlayer().getName().getString())) > 1){
                if (playerEntity.isSpectator()){
                    source.getPlayer().setGameMode(GameMode.SPECTATOR);
                    source.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 999999, 0, false, false));
                    source.getPlayer().addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 999999, 0, false, false));
                    source.sendFeedback(new LiteralText("Remember to use '/s' to go back to survival mode"), false);
                }
                source.getPlayer().teleport(playerEntity.getServerWorld(), playerEntity.getPos().x, playerEntity.getPos().y, playerEntity.getPos().z, source.getPlayer().yaw, source.getPlayer().pitch);
            }
            else source.sendFeedback(new LiteralText("You are not allowed to use this command :P"), false);
        }
        else source.sendFeedback(new LiteralText("This player doesn't exist"), false);
        return 1;
    }
}

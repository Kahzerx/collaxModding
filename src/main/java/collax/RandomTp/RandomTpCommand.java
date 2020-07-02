package collax.RandomTp;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import java.util.Random;

import static net.minecraft.server.command.CommandManager.literal;

public class RandomTpCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(literal("randomCoords").
                requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2)).
                then(CommandManager.argument("playerName", EntityArgumentType.entity()).
                        executes(context -> f0(context.getSource(), EntityArgumentType.getEntity(context, "playerName")))));
    }

    public static int f0(ServerCommandSource source, Entity playerName) throws CommandSyntaxException {
        if (playerName instanceof ServerPlayerEntity){
            Random rand = new Random();
            double X = -10000 + (10000 + 10000) * rand.nextDouble();
            double Z = -10000 + (10000 + 10000) * rand.nextDouble();
            double Y = 255;
            playerName.teleport(X, Y, Z);
            BlockPos pos1 = source.getWorld().getTopPosition(Heightmap.Type.WORLD_SURFACE, playerName.getBlockPos());
            playerName.teleport(X, pos1.getY(), Z);
            ((ServerPlayerEntity) playerName).setSpawnPoint(World.OVERWORLD, playerName.getBlockPos(), true, false);
            ((ServerPlayerEntity) playerName).sendMessage(new LiteralText("Recuerda usar el setHome, más información en !!comandos2"), false);
        }
        return 1;
    }
}

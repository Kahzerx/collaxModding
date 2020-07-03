package collax.where;

import com.google.common.collect.Sets;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import java.util.Collection;
import java.util.Set;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandSource.suggestMatching;

public class WhereCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(literal("where").
                then(CommandManager.argument("player", StringArgumentType.word()).
                        suggests((c, b) -> suggestMatching(getPlayers(c.getSource()), b)).
                        executes(context -> sendLocation(context.getSource(), StringArgumentType.getString(context, "player")))));
    }

    public static int sendLocation(ServerCommandSource source, String player) {
        ServerPlayerEntity playerEntity = source.getMinecraftServer().getPlayerManager().getPlayer(player);
        if (playerEntity instanceof ServerPlayerEntity){
            int player_x = (int) playerEntity.getX();
            int player_y = (int) playerEntity.getY();
            int player_z = (int) playerEntity.getZ();
            String playerPos = Formatting.WHITE + "[x:" + player_x + ", y:" + player_y + ", z:" + player_z + "]";
            String dimension = getDimensionWithColor(playerEntity);
            source.sendFeedback(new LiteralText(Formatting.YELLOW + player + " " + dimension + " " + playerPos), false);
        }
        return 1;
    }

    public static String getDimensionWithColor(ServerPlayerEntity player) {
        Identifier dimensionType = player.world.getDimensionRegistryKey().getValue();
        String msg = player.world.getDimension().toString();
        if (dimensionType.equals(World.OVERWORLD.getValue())) msg = Formatting.GREEN + "[Overworld]";
        else if (dimensionType.equals(World.NETHER.getValue())) msg = Formatting.RED + "[Nether]";
        else if (dimensionType.equals(World.END.getValue())) msg = Formatting.DARK_PURPLE + "[End]";
        return msg;
    }

    private static Collection<String> getPlayers(ServerCommandSource source) {
        Set<String> players = Sets.newLinkedHashSet();
        players.addAll(source.getPlayerNames());
        return players;
    }
}

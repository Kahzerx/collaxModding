package collax.here;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.MessageType;
import net.minecraft.potion.Potion;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import static net.minecraft.server.command.CommandManager.literal;

public class HereCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(literal("here").
                executes(context -> sendLocation(context.getSource())));
    }

    public static int sendLocation(ServerCommandSource source) throws CommandSyntaxException {
        if (source.getPlayer() instanceof ServerPlayerEntity){
            ServerPlayerEntity player = source.getPlayer();
            int player_x = (int) source.getPlayer().getX();
            int player_y = (int) source.getPlayer().getY();
            int player_z = (int) source.getPlayer().getZ();
            String playerPos = Formatting.WHITE + "[x:" + player_x + ", y:" + player_y + ", z:" + player_z + "]";
            String dimension = getDimensionWithColor(player);
            if (player.world.getDimensionRegistryKey().getValue().equals(World.OVERWORLD.getValue())) source.getMinecraftServer().getPlayerManager().broadcastChatMessage(new LiteralText(Formatting.YELLOW + source.getPlayer().getName().asString() + " " + dimension + " " + playerPos + " -> " + Formatting.RED + "[Nether] " + Formatting.WHITE + "(x:" + player_x/8 + ", y:" + player_y + ", z:" + player_z/8 + ")"), MessageType.CHAT, Util.NIL_UUID);
            else if (player.world.getDimensionRegistryKey().getValue().equals(World.NETHER.getValue())) source.getMinecraftServer().getPlayerManager().broadcastChatMessage(new LiteralText(Formatting.YELLOW + source.getPlayer().getName().asString() + " " + dimension + " " + playerPos + " -> " + Formatting.GREEN + "[Overworld] " + Formatting.WHITE + "(x:" + player_x * 8 + ", y:" + player_y + ", z:" + player_z * 8 + ")"), MessageType.CHAT, Util.NIL_UUID);
            else if (player.world.getDimensionRegistryKey().getValue().equals(World.END.getValue())) source.getMinecraftServer().getPlayerManager().broadcastChatMessage(new LiteralText(Formatting.YELLOW + source.getPlayer().getName().asString() + " " + dimension + " " + playerPos), MessageType.CHAT, Util.NIL_UUID);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 100, 0, false, false));
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
}

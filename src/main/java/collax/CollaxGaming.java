package collax;

import collax.back.BackCommand;
import collax.here.HereCommand;
import collax.home.HomeCommand;
import collax.home.SetHomeCommand;
import collax.randomTp.RandomTpCommand;
import collax.discord.DiscordCommand;
import collax.where.WhereCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashMap;

public class CollaxGaming {
    public static String[] bannedWords = {"c", "C", "s", "S", "!!skull", "!!tp", "!!comandos"};

    public static HashMap<String, String> permsArray = new HashMap<>();

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher){
        DiscordCommand.register(dispatcher);
        RandomTpCommand.register(dispatcher);
        HereCommand.register(dispatcher);
        WhereCommand.register(dispatcher);
        SetHomeCommand.register(dispatcher);
        HomeCommand.register(dispatcher);
        BackCommand.register(dispatcher);
    }

    public static String getDimensionWithColor(ServerPlayerEntity player) {
        Identifier dimensionType = player.world.getDimensionRegistryKey().getValue();
        String msg = player.world.getDimension().toString();
        if (dimensionType.equals(World.OVERWORLD.getValue())) msg = Formatting.GREEN + "[Overworld]";
        else if (dimensionType.equals(World.NETHER.getValue())) msg = Formatting.RED + "[Nether]";
        else if (dimensionType.equals(World.END.getValue())) msg = Formatting.DARK_PURPLE + "[End]";
        return msg;
    }

    public static String getDimensionWithColor(World world) {
        Identifier dimensionType = world.getDimensionRegistryKey().getValue();
        String msg = world.getDimension().toString();
        if (dimensionType.equals(World.OVERWORLD.getValue())) msg = Formatting.GREEN + "[Overworld]";
        else if (dimensionType.equals(World.NETHER.getValue())) msg = Formatting.RED + "[Nether]";
        else if (dimensionType.equals(World.END.getValue())) msg = Formatting.DARK_PURPLE + "[End]";
        return msg;
    }

    public static String getDim(World world){
        Identifier dimensionType = world.getDimensionRegistryKey().getValue();
        String msg = world.getDimension().toString();
        if (dimensionType.equals(World.OVERWORLD.getValue())) msg = "Overworld";
        else if (dimensionType.equals(World.NETHER.getValue())) msg = "Nether";
        else if (dimensionType.equals(World.END.getValue())) msg = "End";
        return msg;
    }

    public static String formatCoords(double x, double y, double z){
        return Formatting.WHITE + " [x: " + (int) x + ", y: " + (int) y + ", z: " + (int) z + "]";
    }
}

package collax.spoof;

import collax.CollaxGaming;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.screen.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandSource.suggestMatching;

public class SpoofCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(literal("spoof").
                requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2)).
                then(CommandManager.argument("player", StringArgumentType.word()).
                        suggests((c, b) -> suggestMatching(CollaxGaming.getPlayers(c.getSource()), b)).
                        then(CommandManager.literal("enderChest").
                                executes(context -> spoofEC(context.getSource(), StringArgumentType.getString(context, "player"))))));
    }

    public static int spoofEC(ServerCommandSource source, String playerE) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        ServerPlayerEntity player2 = source.getMinecraftServer().getPlayerManager().getPlayer(playerE);
        if (player2 instanceof ServerPlayerEntity){
            EnderChestInventory enderChestInventory = player2.getEnderChestInventory();
            player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> GenericContainerScreenHandler.createGeneric9x3(i, playerInventory, enderChestInventory), new LiteralText("Ender Chest")));
        }
        else {
            source.sendFeedback(new LiteralText("player offline"), false);
        }
        return 1;
    }
}

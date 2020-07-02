package collax.mixins;

import collax.CollaxGaming;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.WorldBorderCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldBorderCommand.class)
public class WorldBorderCommandMixin {
    @Inject(method = "register", at = @At("RETURN"))
    private static void registerCollax(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo ci){
        CollaxGaming.registerCommand(dispatcher);
    }
}

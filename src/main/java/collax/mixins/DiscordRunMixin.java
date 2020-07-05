package collax.mixins;

import collax.CollaxFileManager;
import collax.discord.DiscordFileManager;
import collax.discord.DiscordListener;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class DiscordRunMixin {
    @Inject(method = "method_29741", at = @At("HEAD"))
    public void run (CallbackInfo ci){
        try {
            String[] result = DiscordFileManager.readFile();
            if (!result[0].equals("") && !result[1].equals("") && !result[2].equals("")) {
                if (result[2].equals("true")) {
                    try {
                        DiscordListener.connect((MinecraftServer) (Object) this, result[0], result[1], "");
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println("config file not created");
        }

        CollaxFileManager.tryCreatePlayerFile();
    }

    @Inject(method = "method_29741", at = @At("RETURN"))
    public void stop (CallbackInfo ci){
        if (DiscordListener.chatBridge) DiscordListener.stop();
    }
}

package collax.mixins;

import collax.discord.DiscordListener;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class DiscordOnDeathMixin extends PlayerEntity {
    public DiscordOnDeathMixin(World world, BlockPos blockPos, GameProfile gameProfile) {
        super(world, blockPos, gameProfile);
    }

    @Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/CombatEventS2CPacket;<init>(Lnet/minecraft/entity/damage/DamageTracker;Lnet/minecraft/network/packet/s2c/play/CombatEventS2CPacket$Type;Lnet/minecraft/text/Text;)V", ordinal = 0))
    public void onPlayerDies(DamageSource source, CallbackInfo ci){
        if (DiscordListener.chatBridge){
            DiscordListener.sendMessage(":skull_crossbones: **" + this.getDamageTracker().getDeathMessage().getString() + "**");
        }
    }
}

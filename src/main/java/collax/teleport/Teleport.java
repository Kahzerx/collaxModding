package collax.teleport;

import collax.discord.DiscordListener;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;
import net.minecraft.text.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Teleport {
    public static HashMap<String, TPAList> tpa = new HashMap<>();
    public static MinecraftServer server;
    public static ArrayList<String> players2remove = new ArrayList<>();
    public static void addPlayer(String player1, String player2){
        tpa.put(player1, new TPAList(player2));
    }

    public static void subTime(){
        tpa.forEach((pl, guanlu) -> {
            guanlu.x--;
            if (guanlu.x <= 0) {
                players2remove.add(pl);
                server.getPlayerManager().getPlayer(guanlu.player);
                ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(guanlu.player);
                assert playerEntity != null;
                playerEntity.sendMessage(new LiteralText("El tpa de " + pl + " ha expirado"), false);
            }
        });
    }

    public static void removePlayers(){
        if (players2remove.size() != 0){
            for (String name : players2remove){
                tpa.remove(name);
            }
            players2remove.clear();
        }
    }

    public static void acceptPlayer(String player1, String player2, MinecraftServer server){
        tpa.forEach((pl, guanlu) -> {
            if (guanlu.player.equals(player1) && pl.equals(player2)){
                ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(player1);
                ServerPlayerEntity playerEntity2 = server.getPlayerManager().getPlayer(player2);
                if (playerEntity instanceof ServerPlayerEntity && playerEntity2 instanceof ServerPlayerEntity){
                    if (playerEntity.isSpectator()){
                        playerEntity2.setGameMode(GameMode.SPECTATOR);
                        playerEntity2.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 999999, 0, false, false));
                        playerEntity2.addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 999999, 0, false, false));
                    }
                    playerEntity2.teleport(playerEntity.getServerWorld(), playerEntity.getPos().x, playerEntity.getPos().y, playerEntity.getPos().z, playerEntity2.yaw, playerEntity2.pitch);
                    playerEntity.sendMessage(new LiteralText("Done :3"), false);
                    playerEntity2.sendMessage(new LiteralText("Te has hecho tp a " + player1), false);
                    players2remove.add(pl);
                }
            }
        });
    }

    public static void denyPlayer(String player1, String player2, MinecraftServer server){
        tpa.forEach((pl, guanlu) -> {
            if (guanlu.player.equals(player1) && pl.equals(player2)){
                ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(player1);
                ServerPlayerEntity playerEntity2 = server.getPlayerManager().getPlayer(player2);
                if (playerEntity instanceof ServerPlayerEntity && playerEntity2 instanceof ServerPlayerEntity){
                    players2remove.add(pl);
                    playerEntity.sendMessage(new LiteralText("Rekt"), false);
                    MutableText denied = new LiteralText("✓✓ ").styled(style -> style.withColor(Formatting.AQUA));
                    Text finalMsg = new LiteralText(player1 + " te ha dejado en veido :(").styled(style -> style.withColor(Formatting.WHITE));
                    playerEntity2.sendMessage(denied.append(finalMsg), false);
                }
            }
        });
    }

    public static Text getMsg(String player){
        Text accept = new LiteralText("/tpa accept " + player).styled((style -> style.withColor(Formatting.GREEN).
                withBold(true).
                withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa accept " + player)).
                setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("tpa accept " + player)))));

        Text deny = new LiteralText("/tpa deny " + player).styled((style -> style.withColor(Formatting.RED).
                withBold(true).
                withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa deny " + player)).
                setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("tpa deny " + player)))));

        return new LiteralText(player + " te ha solicitado tp, usa ").append(accept).append(" para aceptar o ").append(deny).append(" para rechazar").styled((style) -> style.withColor(Formatting.YELLOW));
    }
}

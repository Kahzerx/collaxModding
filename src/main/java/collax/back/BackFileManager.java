package collax.back;

import collax.CollaxGaming;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BackFileManager {
    public static void setDeath(String player, World world, double x, double y, double z){
        try{
            JSONArray playerList = CollaxGaming.getFileContent();
            playerList.forEach(pl -> parsePlayerDeath((JSONObject)pl, player, world, x, y, z));
            CollaxGaming.updateFile();
        } catch (Exception ignored) { }
    }

    public static void parsePlayerDeath(JSONObject playerObj, String player, World world, double x, double y, double z){
        JSONObject playerObject = (JSONObject) playerObj.get("player");
        String name = (String)playerObject.get("name");
        if (name.equals(player)){
            CollaxGaming.updatedPlayerList.add(updateDeathPos(playerObject.get("home"), playerObject.get("name"), playerObject.get("perms"), x, y, z, CollaxGaming.getDim(world)));
        }
        else {
            CollaxGaming.updatedPlayerList.add(playerObj);
        }
    }

    private static JSONObject updateDeathPos(Object home, Object name, Object perms, double x, double y, double z, String dim){
        JSONObject playerInformation = new JSONObject();
        playerInformation.put("home", home);
        playerInformation.put("name", name);
        playerInformation.put("perms", perms);

        JSONObject playerDeathPos = new JSONObject();
        playerDeathPos.put("dim", dim);
        playerDeathPos.put("x", String.valueOf(x));
        playerDeathPos.put("y", String.valueOf(y));
        playerDeathPos.put("z", String.valueOf(z));
        playerInformation.put("death", playerDeathPos);

        JSONObject playersObject = new JSONObject();
        playersObject.put("player", playerInformation);

        return playersObject;
    }

    public static void tpDeathPos(ServerPlayerEntity player){
        JSONArray playerList = CollaxGaming.getFileContent();
        playerList.forEach(pl -> parsePlayerDeathPos((JSONObject)pl, player));
    }

    public static void parsePlayerDeathPos(JSONObject playerObj, ServerPlayerEntity player){
        JSONObject playerObject = (JSONObject) playerObj.get("player");
        String name = (String)playerObject.get("name");
        if (name.equals(player.getName().getString())){
            JSONObject homeObject = (JSONObject)playerObject.get("death");
            String x = (String) homeObject.get("x");
            String y = (String) homeObject.get("y");
            String z = (String) homeObject.get("z");
            String dim = (String) homeObject.get("dim");
            if (x.equals("") || y.equals("") || z.equals("") || dim.equals("")){
                player.sendMessage(new LiteralText("You haven't died yet :("), false);
            }

            else {
                ServerWorld dimension = player.getServer().getWorld(World.OVERWORLD);
                switch (dim){
                    case "Overworld":
                        dimension = player.getServer().getWorld(World.OVERWORLD);
                        break;
                    case "Nether":
                        dimension = player.getServer().getWorld(World.NETHER);
                        break;
                    case "End":
                        dimension = player.getServer().getWorld(World.END);
                        break;
                }
                player.teleport(dimension, Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z), player.yaw, player.pitch);
                player.setGameMode(GameMode.SPECTATOR);
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 999999, 0, false, false));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 999999, 0, false, false));
                player.sendMessage(new LiteralText("Use 's' to go back to survival mode"), false);
            }
        }
    }
}

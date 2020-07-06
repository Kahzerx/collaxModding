package collax.home;

import collax.CollaxFileManager;
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

public class HomeFileManager {

    public static void setHome(ServerPlayerEntity player, World world, double x, double y, double z){
        try{
            JSONArray playerList = CollaxFileManager.getFileContent();
            playerList.forEach(pl -> parsePlayerHome((JSONObject)pl, player, world, x, y, z));
            CollaxFileManager.updateFile();
            player.sendMessage(new LiteralText("Home set: " + CollaxGaming.getDimensionWithColor(world) + CollaxGaming.formatCoords(x, y, z)), false);
        } catch (Exception ignored) { }
    }

    public static void parsePlayerHome(JSONObject playerObj, ServerPlayerEntity player, World world, double x, double y, double z){
        JSONObject playerObject = (JSONObject) playerObj.get("player");
        String name = (String)playerObject.get("name");
        if (name.equals(player.getName().getString())){
            CollaxFileManager.updatedPlayerList.add(updateHomePos(playerObject.get("death"), playerObject.get("name"), playerObject.get("perms"), x, y, z, CollaxGaming.getDim(world)));
        }
        else {
            CollaxFileManager.updatedPlayerList.add(playerObj);
        }
    }

    private static JSONObject updateHomePos(Object death, Object name, Object perms, double x, double y, double z, String dim){
        JSONObject playerInformation = new JSONObject();
        playerInformation.put("death", death);
        playerInformation.put("name", name);
        playerInformation.put("perms", perms);

        JSONObject playerHomePos = new JSONObject();
        playerHomePos.put("dim", dim);
        playerHomePos.put("x", String.valueOf(x));
        playerHomePos.put("y", String.valueOf(y));
        playerHomePos.put("z", String.valueOf(z));
        playerInformation.put("home", playerHomePos);

        JSONObject playersObject = new JSONObject();
        playersObject.put("player", playerInformation);

        return playersObject;
    }


    public static void tpHome(ServerPlayerEntity player){
        JSONArray playerList = CollaxFileManager.getFileContent();
        playerList.forEach(pl -> parsePlayerHome((JSONObject)pl, player));
    }

    public static void parsePlayerHome(JSONObject playerObj, ServerPlayerEntity player){
        JSONObject playerObject = (JSONObject) playerObj.get("player");
        String name = (String)playerObject.get("name");
        if (name.equals(player.getName().getString())){
            JSONObject homeObject = (JSONObject)playerObject.get("home");
            String x = (String) homeObject.get("x");
            String y = (String) homeObject.get("y");
            String z = (String) homeObject.get("z");
            String dim = (String) homeObject.get("dim");
            if (x.equals("") || y.equals("") || z.equals("") || dim.equals("")){
                player.sendMessage(new LiteralText("You need to set up a home first, use /setHome"), false);
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

                if (Integer.parseInt(CollaxGaming.permsArray.get(player.getName().getString())) > 1){
                    player.setGameMode(GameMode.SPECTATOR);
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 999999, 0, false, false));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 999999, 0, false, false));
                    player.sendMessage(new LiteralText("Use '/s' to go back to survival mode"), false);
                }
            }
        }
    }
}

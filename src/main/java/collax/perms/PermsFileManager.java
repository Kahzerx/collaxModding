package collax.perms;

import collax.CollaxFileManager;
import collax.CollaxGaming;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PermsFileManager {

    private static boolean playerFound = false;

    public static void setPerm(ServerPlayerEntity serverPlayerEntity, String player, int val){
        playerFound = false;
        JSONArray playerList = CollaxFileManager.getFileContent();
        playerList.forEach(pl -> parsePlayerPerms((JSONObject)pl, player, val));
        if (playerFound){
            CollaxFileManager.updateFile();
            serverPlayerEntity.sendMessage(new LiteralText(player + " perms = " + val), false);
            if (CollaxGaming.permsArray.containsKey(player)){
                CollaxGaming.permsArray.remove(player);
                CollaxGaming.permsArray.put(player, String.valueOf(val));
            }
        }
        else {
            serverPlayerEntity.sendMessage(new LiteralText("Unable to set perms"), false);
        }
        playerFound = false;
    }

    public static void parsePlayerPerms(JSONObject playerObj, String player, int val){
        JSONObject playerObject = (JSONObject) playerObj.get("player");
        String name = (String)playerObject.get("name");
        if (name.equals(player)){
            CollaxFileManager.updatedPlayerList.add(updatePerms(playerObject.get("death"), playerObject.get("name"), playerObject.get("home"), val));
        }
        else {
            CollaxFileManager.updatedPlayerList.add(playerObj);
        }
    }

    private static JSONObject updatePerms(Object death, Object name, Object home, int value){
        JSONObject playerInformation = new JSONObject();
        playerInformation.put("death", death);
        playerInformation.put("name", name);
        playerInformation.put("home", home);

        playerInformation.put("perms", String.valueOf(value));

        JSONObject playersObject = new JSONObject();
        playersObject.put("player", playerInformation);

        playerFound = true;

        return playersObject;
    }
}

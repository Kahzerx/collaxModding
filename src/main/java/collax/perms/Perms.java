package collax.perms;

import collax.CollaxFileManager;
import collax.CollaxGaming;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Perms {
    public static void addToArray(String player){
        JSONArray playerList = CollaxFileManager.getFileContent();
        playerList.forEach(pl -> parsePlayerPermsJoin((JSONObject)pl, player));
    }

    private static void parsePlayerPermsJoin(JSONObject playerObj, String player){
        JSONObject playerObject = (JSONObject) playerObj.get("player");
        String name = (String)playerObject.get("name");
        if (name.equals(player)){
            String value = (String)playerObject.get("perms");
            CollaxGaming.permsArray.put(name, value);
            System.out.println(CollaxGaming.permsArray);
        }
    }

    public static void removeFromArray(String player){
        JSONArray playerList = CollaxFileManager.getFileContent();
        playerList.forEach(pl -> parsePlayerPermsLeft((JSONObject)pl, player));
    }

    private static void parsePlayerPermsLeft(JSONObject playerObj, String player){
        JSONObject playerObject = (JSONObject) playerObj.get("player");
        String name = (String)playerObject.get("name");
        if (name.equals(player)){
            CollaxGaming.permsArray.remove(name);
            System.out.println(CollaxGaming.permsArray);
        }
    }
}

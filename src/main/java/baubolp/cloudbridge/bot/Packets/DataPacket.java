package baubolp.cloudbridge.bot.Packets;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.HashMap;

public class DataPacket {

    public String password = "!wasfhuagbu89we!";
    public String serverName;
    public String packetName;
    public JSONObject jsonData;

    public DataPacket() {
        this.packetName = "DataPacket";
    }

    public JSONObject getJsonData() {
        return jsonData;
    }

    public String getPacketName() {
        return packetName;
    }

    public String getPassword() {
        return password;
    }

    public String getServerName() {
        return serverName;
    }

    public void handle(JSONObject jsonObject) {
        System.out.println(jsonObject.get("packetName").toString());
    }

    public String encode() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        String encode = JSONValue.toJSONString(hashMap);
        return encode;
    }
}

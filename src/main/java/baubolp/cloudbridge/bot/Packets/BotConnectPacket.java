package baubolp.cloudbridge.bot.Packets;

import baubolp.cloudbridge.bot.Cloud;
import baubolp.cloudbridge.bot.CloudBridge;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.HashMap;

public class BotConnectPacket extends DataPacket {

    public String packetName;
    public String botName;

    public BotConnectPacket() {
        this.packetName = "DiscordBotConnectPacket";
    }

    public String encode() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("password", this.getPassword());
        hashMap.put("packetName", this.packetName);
        hashMap.put("botName", this.botName);
        String encode = JSONValue.toJSONString(hashMap);
        return encode;
    }

    @Override
    public void handle(JSONObject jsonObject) {
        CloudBridge.setCloud(new Cloud(CloudBridge.getClient().getSocket()));
        System.out.println("Eine Verbindung zur Cloud wurde hergestellt.");
    }
}

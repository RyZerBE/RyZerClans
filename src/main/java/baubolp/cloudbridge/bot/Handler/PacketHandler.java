package baubolp.cloudbridge.bot.Handler;

import baubolp.cloudbridge.bot.Packets.DataPacket;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.HashMap;
import java.util.Map;

public class PacketHandler {

    private static Map<String, Class> registeredPackets = new HashMap<String, Class>();

    public static void registerPacket(String name, Class packet) {
        try {
            PacketHandler.registeredPackets.put(name, packet);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregisterPacket(String name) {
        try {
            PacketHandler.registeredPackets.remove(name);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isPacketRegistered(final String packetName) {
        return PacketHandler.registeredPackets.get(packetName) != null;
    }

    public static Map<String, Class> getPackets() {
        return PacketHandler.registeredPackets;
    }

    public static String getPacketNameByRequest(String request) {
        Object obj = JSONValue.parse(request);
        if(obj != null) {
            JSONObject jsonObject = (JSONObject) obj;
            if (jsonObject.get("packetName") != null) {
                return jsonObject.get("packetName").toString();
            }
        }
        System.out.println("Das Paket konnte nicht gehandelt werden! Reason: Kein PacketName vorhanden");
        return "Unknown Packet";
    }
    public static JSONObject handleJsonObject(final String packetName, String input) {
        if(PacketHandler.isPacketRegistered(packetName)) {
            Object obj = JSONValue.parse(input);
            JSONObject jsonObject = (JSONObject) obj;
            return jsonObject;
        }else {
            System.out.println("Das Paket ist nicht registriert!");
        }
        return new JSONObject();
    }

    public static Class getPacketByName(String packetName) {
        return registeredPackets.get(packetName);
    }

    public void handlePacket(JSONObject jsonObject) {
        if(jsonObject.get("packetName") != null) {
            String packetName = jsonObject.get("packetName").toString();
            if(jsonObject.get("password") != null) {
                String password = jsonObject.get("password").toString();
                if (!password.equals("!wasfhuagbu89we!")) {
                    System.out.println("Das Paket hatte ein falsches Passwort!");
                    return;
                }
                Class c = getPacketByName(packetName);
                try {
                    DataPacket packet = (DataPacket) c.newInstance();
                    packet.handle(jsonObject);

                }catch (IllegalAccessException e) {
                    e.printStackTrace();
                }catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("Das Paket hatte kein Passwort!");
            }
        }else {
            System.out.println("Das Paket konnte nicht gehandelt werden! Reason: Kein PacketName vorhanden");
        }
    }

    static {
        PacketHandler.registeredPackets = new HashMap<String, Class>();
    }
}

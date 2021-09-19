package baubolp.cloudbridge.bot;

import baubolp.cloudbridge.bot.Handler.PacketHandler;
import baubolp.cloudbridge.bot.Packets.BotConnectPacket;
import baubolp.cloudbridge.bot.Packets.BotDisconnectPacket;
import baubolp.cloudbridge.bot.Packets.BotKeepAlivePacket;
import baubolp.cloudbridge.bot.Packets.ClanWarResultPacket;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CloudBridge {

    private static Client client;
    private static PacketHandler packetHandler;
    private boolean start = false;
    private static Cloud cloud = null;
    private static String botName;

    public void start(String bot) throws Exception {
        if(start) {
          throw new Exception("CloudBridge already started!");
        }
        System.out.println("RRRRRRRRRRRRRRRRR                                                                                        CCCCCCCCCCCClllllll                                             d::::::d\n" +
                "R::::::::::::::::R                                                                                    CCC::::::::::::l:::::l                                             d::::::d\n" +
                "R::::::RRRRRR:::::R                                                                                 CC:::::::::::::::l:::::l                                             d::::::d\n" +
                "RR:::::R     R:::::R                                                                               C:::::CCCCCCCC::::l:::::l                                             d:::::d \n" +
                "  R::::R     R:::::yyyyyyy           yyyyyyzzzzzzzzzzzzzzzzz   eeeeeeeeeeee   rrrrr   rrrrrrrrr   C:::::C       CCCCCCl::::l   ooooooooooo  uuuuuu    uuuuuu     ddddddddd:::::d \n" +
                "  R::::R     R:::::Ry:::::y         y:::::yz:::::::::::::::z ee::::::::::::ee r::::rrr:::::::::r C:::::C              l::::l oo:::::::::::oou::::u    u::::u   dd::::::::::::::d \n" +
                "  R::::RRRRRR:::::R  y:::::y       y:::::y z::::::::::::::z e::::::eeeee:::::er:::::::::::::::::rC:::::C              l::::lo:::::::::::::::u::::u    u::::u  d::::::::::::::::d \n" +
                "  R:::::::::::::RR    y:::::y     y:::::y  zzzzzzzz::::::z e::::::e     e:::::rr::::::rrrrr::::::C:::::C              l::::lo:::::ooooo:::::u::::u    u::::u d:::::::ddddd:::::d \n" +
                "  R::::RRRRRR:::::R    y:::::y   y:::::y         z::::::z  e:::::::eeeee::::::er:::::r     r:::::C:::::C              l::::lo::::o     o::::u::::u    u::::u d::::::d    d:::::d \n" +
                "  R::::R     R:::::R    y:::::y y:::::y         z::::::z   e:::::::::::::::::e r:::::r     rrrrrrC:::::C              l::::lo::::o     o::::u::::u    u::::u d:::::d     d:::::d \n" +
                "  R::::R     R:::::R     y:::::y:::::y         z::::::z    e::::::eeeeeeeeeee  r:::::r           C:::::C              l::::lo::::o     o::::u::::u    u::::u d:::::d     d:::::d \n" +
                "  R::::R     R:::::R      y:::::::::y         z::::::z     e:::::::e           r:::::r            C:::::C       CCCCCCl::::lo::::o     o::::u:::::uuuu:::::u d:::::d     d:::::d \n" +
                "RR:::::R     R:::::R       y:::::::y         z::::::zzzzzzze::::::::e          r:::::r             C:::::CCCCCCCC::::l::::::o:::::ooooo:::::u:::::::::::::::ud::::::ddddd::::::dd\n" +
                "R::::::R     R:::::R        y:::::y         z::::::::::::::ze::::::::eeeeeeee  r:::::r              CC:::::::::::::::l::::::o:::::::::::::::ou:::::::::::::::ud:::::::::::::::::d\n" +
                "R::::::R     R:::::R       y:::::y         z:::::::::::::::z ee:::::::::::::e  r:::::r                CCC::::::::::::l::::::loo:::::::::::oo  uu::::::::uu:::u d:::::::::ddd::::d\n" +
                "RRRRRRRR     RRRRRRR      y:::::y          zzzzzzzzzzzzzzzzz   eeeeeeeeeeeeee  rrrrrrr                   CCCCCCCCCCCCllllllll  ooooooooooo      uuuuuuuu  uuuu  ddddddddd   ddddd\n" +
                "                         y:::::y                                                                                                                                                 \n" +
                "                        y:::::y                                                                                                                                                  \n" +
                "                       y:::::y                                                                                                                                                   \n" +
                "                      y:::::y                                                                                                                                                    \n" +
                "                     yyyyyyy  ");

        packetHandler = new PacketHandler();
        botName = bot;
        start = true;
        registerPackets();
        createClient("5.181.151.61", 7000);
        requestCloud();
        startChecks();
        startRequestTask();
    }

    public void stop() throws Exception {
        if(!start) {
            throw new Exception("CloudBridge isn't started!");
        }
        BotDisconnectPacket disconnectPacket = new BotDisconnectPacket();
        disconnectPacket.botName = getBotName();
        getCloud().sendPacket(disconnectPacket);
    }

    public void createClient(String ip, int port) throws Exception {
        if(!start) {
            throw new Exception("CloudBridge isn't started!");
        }
        if(client != null) {
            client = client.restartConnection();
        }else {
            client = new Client(ip, port);
        }
    }

    private void startRequestTask() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for(String request : new ArrayList<>(CloudBridge.getClient().getQueue())) {
                    CloudBridge.getPacketHandler().handlePacket(PacketHandler.handleJsonObject(PacketHandler.getPacketNameByRequest(request), request));
                    CloudBridge.getClient().getQueue().remove(request);
                }
            }
        }, 500L, 500L);
    }

    private void requestCloud() {
        try {
            Socket socket = CloudBridge.getClient().getSocket();

            BotConnectPacket cloudPacket = new BotConnectPacket();
            cloudPacket.botName = getBotName();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            bufferedWriter.write(cloudPacket.encode());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startChecks() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(CloudBridge.getCloud() == null) {
                    System.out.println("Es konnte keine Verbindung zur Cloud hergestellt werden.");
                    System.out.println("Es konnte keine Verbindung zur Cloud hergestellt werden.");
                    System.out.println("Es konnte keine Verbindung zur Cloud hergestellt werden.");
                    requestCloud();
                    return;
                }
                Cloud cloud = CloudBridge.getCloud();
                if(cloud.getKeepAliveChecks() == 0) {
                    BotKeepAlivePacket packet = new BotKeepAlivePacket();
                    packet.botName = getBotName();
                    cloud.sendPacket(packet);

                    cloud.setKeepAliveChecks(cloud.getKeepAliveChecks() + 1);
                }else if(cloud.getKeepAliveChecks() > 1) {
                    System.out.println("Es konnte keine Verbindung zur Cloud hergestellt werden.");
                    System.out.println("Es konnte keine Verbindung zur Cloud hergestellt werden.");
                    System.out.println("Es konnte keine Verbindung zur Cloud hergestellt werden.");
                    CloudBridge.getClient().restartConnection();
                }else {
                    System.out.println("Verbindung konnte nicht hergestellt werden. Versuche erneut...");
                    cloud.setKeepAliveChecks(cloud.getKeepAliveChecks() + 1);
                }
            }
        }, 10000L, 10000L);
    }

    public static Client getClient() {
        return client;
    }

    public static PacketHandler getPacketHandler() {
        return packetHandler;
    }

    public static Cloud getCloud() {
        return cloud;
    }

    public static void setCloud(Cloud cloud) {
        CloudBridge.cloud = cloud;
    }

    private static void registerPackets() {
        PacketHandler.registerPacket("DiscordBotConnectPacket", BotConnectPacket.class);
        PacketHandler.registerPacket("BotKeepAlivePacket", BotKeepAlivePacket.class);
        PacketHandler.registerPacket("DiscordBotDisconnectPacket", BotDisconnectPacket.class);
        PacketHandler.registerPacket("ClanWarResultPacket", ClanWarResultPacket.class);
    }

    public static String getBotName() {
        return botName;
    }
}

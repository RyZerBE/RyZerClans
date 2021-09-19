package baubolp.cloudbridge.bot;

import baubolp.cloudbridge.bot.Packets.DataPacket;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Cloud {


    private Socket cloudSocket;
    private int keepAliveChecks;

    public Cloud(Socket socket) {
        this.cloudSocket = socket;
    }

    public Socket getCloudSocket() {
        return cloudSocket;
    }

    public void sendPacket(DataPacket dataPacket) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(getCloudSocket().getOutputStream()));
            bufferedWriter.write(dataPacket.encode());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getKeepAliveChecks() {
        return keepAliveChecks;
    }

    public void setKeepAliveChecks(int keepAliveChecks) {
        this.keepAliveChecks = keepAliveChecks;
    }
}

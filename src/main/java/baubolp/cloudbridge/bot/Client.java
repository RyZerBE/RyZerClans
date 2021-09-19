package baubolp.cloudbridge.bot;

import baubolp.cloudbridge.bot.Packets.DataPacket;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Thread {

    private Socket socket;
    private ArrayList<String> queue;
    private boolean stop;

    private String ip;
    private int port;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            this.stop = false;
            this.queue = new ArrayList<>();
            this.socket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.start();
    }

    public Socket getSocket() {
        return socket;
    }

    public void run() {
        while(!this.stop) {
            try {
                DataInputStream inputStream = new DataInputStream(this.socket.getInputStream());
                String request = inputStream.readLine();
                if(request != null) {
                    this.queue.add(request);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeData(DataPacket dataPacket) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            bufferedWriter.write(dataPacket.encode());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopClient() {
        this.stop = true;
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Client restartConnection() {
        this.stopClient();
        return new Client(this.ip, this.port);
    }

    public ArrayList<String> getQueue() {
        return queue;
    }
}

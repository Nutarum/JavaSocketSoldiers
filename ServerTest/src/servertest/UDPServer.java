package servertest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;

class UDPServer implements Runnable {

    public ArrayList<String> commandList = new ArrayList();

    static int port = 1234;

    static DatagramSocket serverSocket;
    byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];

    public UDPServer() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            serverSocket = new DatagramSocket(port);
        } catch (SocketException ex) {
        }
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        Globals.serverLog("Port: " + port);
        try {
            InetAddress IP;
            IP = InetAddress.getLocalHost();
            Globals.serverLog("Local IP: " + IP.getHostAddress());
        } catch (UnknownHostException ex) {
        }
        URL whatismyip;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            Globals.serverLog("Web IP: " + ip);
        } catch (Exception ex) {
            Globals.serverLog("Web IP not found");
        }

        while (true) {

            try {
                //sin la sig linea, guarda el final del mensaje anterior (si este era mas largo)
                receivePacket.setData(new byte[1024]);
                serverSocket.receive(receivePacket);
                String sentence = new String(receivePacket.getData());
                commandList.add(receivePacket.getAddress().toString().split("/")[1] + ":" + receivePacket.getPort() + "/" + sentence);
            } catch (IOException ex) {
            }

        }
    }

    public void SendWorld(ArrayList<Client> clientList,ArrayList<Flag> flagList, String ip, int port) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);

            String worldString = "";
            for (int i = 0; i < flagList.size(); i++) {
                worldString+= flagList.get(i).team;
                worldString += ">";
            }
            worldString += "º";

            for (int i = 0; i < clientList.size(); i++) {
                worldString += clientList.get(i).team + "_" + clientList.get(i).x + "_" + clientList.get(i).y + "_";

                if (clientList.get(i).shoots.size() == 0) {
                    worldString += " ";
                }

                for (int j = 0; j < clientList.get(i).shoots.size(); j++) {
                    worldString += (int) clientList.get(i).shoots.get(j).x + "," + (int) clientList.get(i).shoots.get(j).y + ".";
                }

                worldString += "/";
            }

            sendData = worldString.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
            try {
                serverSocket.send(sendPacket);
            } catch (IOException ex) {
            }

        } catch (UnknownHostException ex) {
        }

    }

}

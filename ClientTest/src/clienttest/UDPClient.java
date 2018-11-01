package clienttest;

import java.io.*;
import java.net.*;

class UDPClient implements Runnable {

    private static int port=0;
    private static String serverIP="";

    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    static DatagramSocket clientSocket;
    static InetAddress IPAddress;
    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];

    public UDPClient() {
        File archivo = new File("config.txt");
        FileReader fr;
        try {
            fr = new FileReader(archivo);

            BufferedReader br = new BufferedReader(fr);
           
            while(serverIP.startsWith("#") || serverIP.equals("")){
                serverIP = br.readLine();
               
            }
            
            String portString="";
            while(portString.startsWith("#") || portString.equals("")){
                portString = br.readLine();         
                 
            }
            port=Integer.parseInt(portString);
        } catch (Exception ex) {
        }
        
        Thread thread = new Thread(this);
        thread.start();
    }

    public void sendCommands(String commands) {
        commands += " ";
        sendData = commands.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
        try {
            clientSocket.send(sendPacket);
        } catch (IOException ex) {
        }
    }

    @Override
    public void run() {
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException ex) {
        }
        try {
            IPAddress = InetAddress.getByName(serverIP);
        } catch (UnknownHostException ex) {
        }
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        while (true) {
            try {
                //sin la sig linea, guarda el final del mensaje anterior (si este era mas largo)
                receivePacket.setData(new byte[1024]);
                clientSocket.receive(receivePacket);
                String serverMessage = new String(receivePacket.getData());
                generateWorld(serverMessage);
            } catch (IOException ex) {
            }
        }
        //clientSocket.close();
    }

    private void generateWorld(String serverMessage) {
        World.playerList.clear();
       
        World.flagList.get(0).team = Integer.parseInt(serverMessage.split(">")[0]);
        World.flagList.get(1).team = Integer.parseInt(serverMessage.split(">")[1]);
       
       
     
       
        serverMessage=serverMessage.split("º")[1];
        
        String[] players = serverMessage.split("/");
        for (int i = 0; i < players.length - 1; i++) {

            World.playerList.add(new Player(Integer.parseInt(players[i].split("_")[0]), Integer.parseInt(players[i].split("_")[1]), Integer.parseInt(players[i].split("_")[2])));

            if (players[i].split("_")[3].contains(".")) {
                String[] shoots = players[i].split("_")[3].split("\\.");
                for (int j = 0; j < shoots.length; j++) {
                    World.playerList.get(i).shoots.add(new Shoot(Integer.parseInt(shoots[j].split(",")[0]), Integer.parseInt(shoots[j].split(",")[1])));
                }
            }

        }
    }
}

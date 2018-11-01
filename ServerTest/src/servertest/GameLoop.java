package servertest;

import java.util.ArrayList;
import java.util.Random;

public class GameLoop implements Runnable {

    static UDPServer udpserver;
    static ClientController clientController;

    public static ArrayList<String> commandList = new ArrayList();

    public GameLoop() {
        udpserver = new UDPServer();
        clientController = new ClientController();

        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {

            long startTime = System.currentTimeMillis();

            //PROCESA COMANDOS
            commandList.clear();
            commandList.addAll(udpserver.commandList);
            udpserver.commandList.clear();

            for (int i = 0; i < commandList.size(); i++) {

                boolean newClient = true;
                for (int j = 0; j < clientController.clientList.size(); j++) {
                    if (commandList.get(i).startsWith(clientController.clientList.get(j).ip + ":" + clientController.clientList.get(j).port)) {
                        clientController.clientList.get(j).timeout = 0;
                        newClient = false;

                        if (commandList.get(i).contains("w")) {
                            clientController.clientList.get(j).w = true;
                        }
                        if (commandList.get(i).contains("a")) {
                            clientController.clientList.get(j).a = true;
                        }
                        if (commandList.get(i).contains("s")) {
                            clientController.clientList.get(j).s = true;
                        }
                        if (commandList.get(i).contains("d")) {
                            clientController.clientList.get(j).d = true;
                        }
                        clientController.clientList.get(j).clicks = commandList.get(i).split("C")[1];

                    }
                }

                if (newClient == true) {
                    clientController.addClient(commandList.get(i).split(":")[0], Integer.parseInt(commandList.get(i).split(":")[1].split("/")[0]));
                }
            }

            clientController.TimeOut();

            //MUEVE MUNDO
            //Mueve players
            movePlayers();

            //ENVIA MUNDO
            for (int i = 0; i < clientController.clientList.size(); i++) {

                udpserver.SendWorld(clientController.clientList,clientController.flagList, clientController.clientList.get(i).ip, clientController.clientList.get(i).port);

            }
            
            long tiempoBucle = System.currentTimeMillis() - startTime;            
            if (tiempoBucle < 50) {
                try {
                    Thread.sleep(50 - tiempoBucle);
                } catch (InterruptedException ex) {
                }
            } else {
                Globals.serverLog("SERVER LOOP MUY LENTO!");
            }
        }
    }

    private void movePlayers() {

        for (int i = 0; i < clientController.clientList.size(); i++) {

            if (clientController.clientList.get(i).w && clientController.clientList.get(i).d) {

                double ms2 = Math.sqrt(Math.pow(clientController.clientList.get(i).moveSpeed, 2) + Math.pow(clientController.clientList.get(i).moveSpeed, 2)) / 2;
                clientController.clientList.get(i).x += ms2;
                clientController.clientList.get(i).y -= ms2;

            } else if (clientController.clientList.get(i).d && clientController.clientList.get(i).s) {

                double ms2 = Math.sqrt(Math.pow(clientController.clientList.get(i).moveSpeed, 2) + Math.pow(clientController.clientList.get(i).moveSpeed, 2)) / 2;
                clientController.clientList.get(i).x += ms2;
                clientController.clientList.get(i).y += ms2;

            } else if (clientController.clientList.get(i).s && clientController.clientList.get(i).a) {

                double ms2 = Math.sqrt(Math.pow(clientController.clientList.get(i).moveSpeed, 2) + Math.pow(clientController.clientList.get(i).moveSpeed, 2)) / 2;
                clientController.clientList.get(i).x -= ms2;
                clientController.clientList.get(i).y += ms2;

            } else if (clientController.clientList.get(i).a && clientController.clientList.get(i).w) {

                double ms2 = Math.sqrt(Math.pow(clientController.clientList.get(i).moveSpeed, 2) + Math.pow(clientController.clientList.get(i).moveSpeed, 2)) / 2;
                clientController.clientList.get(i).x -= ms2;
                clientController.clientList.get(i).y -= ms2;
            } else if (clientController.clientList.get(i).w) {

                clientController.clientList.get(i).y -= clientController.clientList.get(i).moveSpeed;
            } else if (clientController.clientList.get(i).a) {

                clientController.clientList.get(i).x -= clientController.clientList.get(i).moveSpeed;
            } else if (clientController.clientList.get(i).s) {

                clientController.clientList.get(i).y += clientController.clientList.get(i).moveSpeed;
            } else if (clientController.clientList.get(i).d) {

                clientController.clientList.get(i).x += clientController.clientList.get(i).moveSpeed;
            }
            
            //Colision con banderas
            for(int j=0;j<clientController.flagList.size();j++){
                if(Globals.collision(clientController.clientList.get(i).x, clientController.clientList.get(i).y, clientController.clientList.get(i).width, clientController.clientList.get(i).height, clientController.flagList.get(j).x, clientController.flagList.get(j).y, clientController.flagList.get(j).width, clientController.flagList.get(j).height)){
                    if(clientController.clientList.get(i).team==1 && clientController.flagList.get(j).team > -300){
                        clientController.flagList.get(j).team-=3;
                    }else if(clientController.clientList.get(i).team==2 && clientController.flagList.get(j).team < 300){
                         clientController.flagList.get(j).team+=3;
                    }
                }
            }          

            clientController.clientList.get(i).firecd--;
            if (clientController.clientList.get(i).clicks.startsWith("1 ")) {
                clientController.clientList.get(i).fire(Integer.parseInt(clientController.clientList.get(i).clicks.split(" ")[1]), Integer.parseInt(clientController.clientList.get(i).clicks.split(" ")[2]));
            } else if (clientController.clientList.get(i).clicks.startsWith("2 ")) {
                clientController.clientList.get(i).grenade(Integer.parseInt(clientController.clientList.get(i).clicks.split(" ")[1]), Integer.parseInt(clientController.clientList.get(i).clicks.split(" ")[2]));
            } else if (clientController.clientList.get(i).clicks.startsWith("12 ")) {
                clientController.clientList.get(i).fire(Integer.parseInt(clientController.clientList.get(i).clicks.split(" ")[1]), Integer.parseInt(clientController.clientList.get(i).clicks.split(" ")[2]));
                clientController.clientList.get(i).grenade(Integer.parseInt(clientController.clientList.get(i).clicks.split(" ")[1]), Integer.parseInt(clientController.clientList.get(i).clicks.split(" ")[2]));
            }

            clientController.clientList.get(i).w = false;
            clientController.clientList.get(i).a = false;
            clientController.clientList.get(i).s = false;
            clientController.clientList.get(i).d = false;
            clientController.clientList.get(i).clicks = "";

            clientController.clientList.get(i).moveShots(clientController.clientList);
        }
         //equipo gana?
        boolean team1gana = true;
        boolean team2gana = true;
         for(int i=0;i<clientController.flagList.size();i++){
              if(clientController.flagList.get(i).team != 300){
                  team1gana=false;
              }
              if(clientController.flagList.get(i).team != -300){
                  team2gana=false;
              }
         }
         if(team1gana){
             resetWorld();
         }else if(team2gana){
             resetWorld();
         }
    }

    private void resetWorld() {
        for(int i=0;i<clientController.flagList.size();i++){
              clientController.flagList.get(i).team=0;              
         }
        for (int i = 0; i < clientController.clientList.size(); i++) {
            clientController.clientList.get(i).shoots.clear();
            Random r = new Random();
                        clientController.clientList.get(i).x = r.nextInt(600) + 100;
                        clientController.clientList.get(i).y = r.nextInt(400) + 100;
        }
    }
}

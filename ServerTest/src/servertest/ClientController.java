package servertest;

import java.util.ArrayList;
import java.util.Random;

public class ClientController {

    public ArrayList<Client> clientList;
    public ArrayList<Flag> flagList;
 
    int team1 = 0;
    int team2 = 0;

    public ClientController() {
        clientList = new ArrayList<>();
        
        flagList = new ArrayList<>();        
        flagList.add(new Flag(100,100));
         flagList.add(new Flag(600,500));
    }

    public void addClient(String ip, int port) {
        int team;
        if (team1 < team2) {
            team = 1;
            team1++;
        } else if (team1 > team2) {
            team = 2;
            team2++;
        } else {
            Random r = new Random();
            team = r.nextInt(2);
            if (team == 0) {
                team = 1;
                team1++;
            } else if (team == 1) {
                team = 2;
                team2++;
            }
        }

        clientList.add(new Client(ip, port, team));
        Globals.serverLog("Client added");
    }

    //DE MOMENTO NO SE USA
    public int removeClient(String ip, int port) {
        for (int i = 0; i < clientList.size(); i++) {
            if (clientList.get(i).ip.equals(ip) && clientList.get(i).port == port) {             
                if(clientList.get(i).team==1){
                    team1--;
                }else{
                    team2--;
                }
                clientList.remove(i);
                return 0;
            }
        }
        return 0;
    }

    void TimeOut() {
        for (int i = clientList.size() - 1; i > -1; i--) {
            if (clientList.get(i).timeout > 100) {
                  if(clientList.get(i).team==1){
                    team1--;
                }else{
                    team2--;
                }
                clientList.remove(i);
                Globals.serverLog("Client remmoved");
            } else {
                clientList.get(i).timeout++;
            }
        }
    }
}

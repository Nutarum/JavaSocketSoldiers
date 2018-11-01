package servertest;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class Client {

    public int team;
    
    public Vector<Shoot> shoots = new Vector<>();
    public int firecd;

    public String ip;
    public int port;
    public int timeout = 0;

    public int x = 400;
    public int y = 300;
    public double moveSpeed = 7;
    
    public int width = 20;
    public int height = 20;

    public boolean w;
    public boolean a;
    public boolean s;
    public boolean d;
    public String clicks = "";

    public Client(String ip, int port, int team) {
        this.ip = ip;
        this.port = port;
        this.team=team;
        
    }

    void fire(int targetX, int targetY) {
        if (firecd < 0) {
            shoots.add(new Shoot(x + 10, y + 10, targetX, targetY));
            firecd = 3;
        }
    }

    void grenade(int targetX, int targetY) {

    }

    void moveShots(ArrayList<Client> clientList) {
        for (int i = shoots.size() - 1; i > -1; i--) {      
            shoots.get(i).x += shoots.get(i).vx;
            shoots.get(i).y += shoots.get(i).vy;

            boolean removed = false;
            for (int j = 0; j < clientList.size(); j++) {
                if (clientList.get(j) == this) {

                } else {
                    if (Globals.collision((int) shoots.get(i).x, (int) shoots.get(i).y, 2, 2, clientList.get(j).x, clientList.get(j).y, clientList.get(j).width, clientList.get(j).height)) {

                        Random r = new Random();
                        clientList.get(j).x = r.nextInt(600) + 100;
                        clientList.get(j).y = r.nextInt(400) + 100;
                        shoots.remove(i);
                        removed = true;
                        j=clientList.size();
                    }
                }

            }
            if (!removed) {
                shoots.get(i).time++;
                if (shoots.get(i).time > 30) {
                    shoots.remove(i);
                }
            }
        }
    }

}

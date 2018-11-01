package clienttest;

import java.util.Vector;

public class Player {

    public int team;
    public int x;
    public int y;
  public Vector<Shoot> shoots = new Vector<>();
    
    public Player( int team, int x, int y) {
        this.team=team;
        this.x = x;
        this.y = y;
    }
}

package clienttest;

import clienttest.Main.GamePanel;

public class Loop implements Runnable {

    public static GamePanel jpanel;

    public Loop(GamePanel jpanel) {
        this.jpanel = jpanel;
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        while (true) {
            long startTime = System.currentTimeMillis();
            jpanel.repaint();
            long tiempoBucle = System.currentTimeMillis() - startTime;
 
            if (tiempoBucle < 50) {
                try {
                    Thread.sleep(50 - tiempoBucle);
                } catch (InterruptedException ex) {
                }
            }
        }

    }

}

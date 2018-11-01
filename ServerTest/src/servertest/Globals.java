package servertest;

import java.util.Date;

public class Globals {

    public static javax.swing.JTextArea serverLogText;

    public static void serverLog(String text) {
        //serverLogText.append(text + "\n");        
        Date date = new Date(System.currentTimeMillis());
        serverLogText.setText(date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + " - " + text + "\n" + serverLogText.getText());
    }

    static boolean collision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
 
        if(x1+w1>x2 && x1<x2+w2 && y1+w1>y2 && y1<y2+w2){
           return true;
        }
      
            
        
        return false;
    }
}

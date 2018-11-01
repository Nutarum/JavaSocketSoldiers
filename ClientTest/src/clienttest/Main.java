package clienttest;

import clienttest.Main.GamePanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

    boolean w = false;
    boolean a = false;
    boolean s = false;
    boolean d = false;
    String clicks = "";

    UDPClient udpClient;

    public static void main(String args[]) {
        new Main();
        World.flagList.add(new Flag(100,100));
        World.flagList.add(new Flag(600,500));
    }

    public Main() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                udpClient = new UDPClient();

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("The Game");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());

                GamePanel jpanel = new GamePanel();
                frame.add(jpanel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setResizable(false);

                Loop loop = new Loop(jpanel);

                frame.addKeyListener(new KeyAdapter() {

                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyChar() == 'w') {
                            w = true;
                        } else if (e.getKeyChar() == 'a') {
                            a = true;
                        } else if (e.getKeyChar() == 's') {
                            s = true;
                        } else if (e.getKeyChar() == 'd') {
                            d = true;
                        }
                    }

                    public void keyReleased(KeyEvent e) {
                        if (e.getKeyChar() == 'w') {
                            w = false;
                        } else if (e.getKeyChar() == 'a') {
                            a = false;
                        } else if (e.getKeyChar() == 's') {
                            s = false;
                        } else if (e.getKeyChar() == 'd') {
                            d = false;
                        }
                    }
                });
            }
        });

    }

    public class GamePanel extends JPanel {

        boolean leftPressed = false;
        boolean rigthPressed = false;
        int mouseX = 0;
        int mouseY = 0;

        public GamePanel() {
            
            this.addMouseMotionListener(new MouseAdapter() {

                @Override
                public void mouseDragged(MouseEvent e) {
                    mouseX = e.getPoint().x;
                    mouseY = e.getPoint().y;
                }
            });

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    mouseX = e.getPoint().x;
                    mouseY = e.getPoint().y;
                    if (e.getButton() == 1) {
                        leftPressed = true;
                    } else if (e.getButton() == 3) {
                        rigthPressed = true;
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.getButton() == 1) {
                        leftPressed = false;
                    }
                    if (e.getButton() == 3) {
                        rigthPressed = false;
                    }
                }
            });

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 600);
        }

        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 810, 610);

             for (int i = 0; i < World.flagList.size(); i++) {
                 g.setColor(Color.BLACK);
                 g.drawRect(World.flagList.get(i).x, World.flagList.get(i).y, World.flagList.get(i).width, World.flagList.get(i).height);
                 if(World.flagList.get(i).team<0){
                      g.setColor(Color.MAGENTA);
                      g.fillRect(World.flagList.get(i).x, World.flagList.get(i).y, -World.flagList.get(i).team/10, World.flagList.get(i).height);
                 }else{
                       g.setColor(Color.BLUE);
                      g.fillRect(World.flagList.get(i).x, World.flagList.get(i).y, World.flagList.get(i).team/10, World.flagList.get(i).height);
                
                 }
             }
             
            for (int i = 0; i < World.playerList.size(); i++) {
                
                if(World.playerList.get(i).team==1){
                    g.setColor(Color.MAGENTA);
                }else{
                    g.setColor(Color.BLUE);
                }               
                
                g.fillRect(World.playerList.get(i).x, World.playerList.get(i).y, 20, 20);
                
                for (int j = 0; j < World.playerList.get(i).shoots.size(); j++) {
                    g.setColor(Color.RED);
                    g.fillRect(World.playerList.get(i).shoots.get(j).x, World.playerList.get(i).shoots.get(j).y, 2, 2);
                }
            }

            // g.setColor(Color.BLACK);
            // g.fillOval(100, 100, 30, 30);
            sendCommands();
        }

        private void sendCommands() {

            String commands = "";
            if (w) {
                commands += "w_";
            }
            if (a) {
                commands += "a_";
            }
            if (s) {
                commands += "s_";
            }
            if (d) {
                commands += "d_";
            }

            clicks = "C";

            if (leftPressed) {
                clicks += "1";
            }
            if (rigthPressed) {
                clicks += "2";
            }
            if (clicks.equals("C")) {
                clicks += "0";
            }

            clicks += " " + mouseX + " " + mouseY;

            commands += clicks;

            udpClient.sendCommands(commands);
        }
    }
}

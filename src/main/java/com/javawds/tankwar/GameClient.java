package com.javawds.tankwar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameClient extends JComponent {

    private Tank playerTank;

    private GameClient(){
        this.playerTank = new Tank(400, 100, Direction.LEFT);
        this.setPreferredSize(new Dimension(800,600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        playerTank.draw(g);
        //g.drawImage(this.playerTank.getImage(),this.playerTank.getX(), this.playerTank.getY(),null);
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setTitle("tank war!");
        frame.setIconImage(new ImageIcon("assets/images/tank.png").getImage());
        final GameClient client = new GameClient();
        client.repaint();
        frame.add(client);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //boolean u = false, d = false, l = false, r = false;
                client.playerTank.keyPressed(e);

            }
            @Override
            public void keyReleased(KeyEvent e) {
                client.playerTank.keyRelease(e);
            }
        });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        while (true) {
            client.repaint();
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

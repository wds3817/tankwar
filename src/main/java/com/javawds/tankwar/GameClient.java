package com.javawds.tankwar;

import javax.swing.*;
import java.awt.*;

public class GameClient extends JComponent {

    private GameClient(){
        this.setPreferredSize(new Dimension(800,600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        g.drawImage(new ImageIcon("assets/images/tankD.gif").getImage(),400, 100,null);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("tank war!");
        frame.setIconImage(new ImageIcon("assets/images/tank.png").getImage());
        GameClient client = new GameClient();
        client.repaint();
        frame.add(client);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

package com.javawds.tankwar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameClient extends JComponent {

    private static final GameClient INSTANCE = new GameClient();

     static GameClient getInstance() {
        return INSTANCE;
    }

    private Tank playerTank;

     List<Tank> getEnemyTanks() {
        return enemyTanks;
    }

    private List<Tank> enemyTanks;

     List<Wall> getWalls() {
        return walls;
    }

    private List<Wall> walls;

    List<Missile> getMissiles() {
        return missiles;
    }

    private List<Missile> missiles;

    synchronized void add(Missile missile) {
        missiles.add(missile);
    }
    void removeMissile(Missile missile) {
        missiles.remove(missile);
    }

    public Tank getPlayerTank() {
        return playerTank;
    }
    private List<Explosion> explosions;

    public List<Explosion> getExplosions() {
        return explosions;
    }
    void addExplosion(Explosion explosion) {
        explosions.add(explosion);
    }

    private GameClient(){
        this.playerTank = new Tank(400, 100, Direction.LEFT);
        this.missiles = new ArrayList<>();
        this.explosions = new ArrayList<>();
        this.walls = Arrays.asList(
                new Wall(200, 140, true, 15),
                new Wall(200, 540, true, 15),
                new Wall(100, 80, false, 15),
                new Wall(700, 80, false, 15)
        );
        this.initEnemyTanks();
        this.setPreferredSize(new Dimension(800,600));
    }

    private void initEnemyTanks() {
        this.enemyTanks = new ArrayList<>(12);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                this.enemyTanks.add(new Tank(200 + j * 120, 400 + 40 * i, true, Direction.UP));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, 800, 600);
        playerTank.draw(g);
        enemyTanks.removeIf(t -> !t.isLive());
        if (enemyTanks.isEmpty()) {
            this.initEnemyTanks();
        }
        for (Tank tank : enemyTanks) {
            tank.draw(g);
        }
        for (Wall wall : walls) {
            wall.draw(g);
        }
        missiles.removeIf(m -> !m.isLive());
        for (Missile missile : missiles) {
            missile.draw(g);
        }
        explosions.removeIf(e -> !e.isLive());
        for (Explosion explosion : explosions) {
            explosion.draw(g);
        }
        //g.drawImage(this.playerTank.getImage(),this.playerTank.getX(), this.playerTank.getY(),null);
    }

    public static void main(String[] args) {
        com.sun.javafx.application.PlatformImpl.startup(()->{});
        final JFrame frame = new JFrame();
        frame.setTitle("tank war!");
        frame.setIconImage(new ImageIcon("assets/images/tank.png").getImage());
        final GameClient client = GameClient.getInstance();
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

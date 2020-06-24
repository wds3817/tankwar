package com.javawds.tankwar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GameClient extends JComponent {

    private static final GameClient INSTANCE = new GameClient();

     static GameClient getInstance() {
        return INSTANCE;
    }

    private Tank playerTank;

     private final AtomicInteger enemyKilled = new AtomicInteger(0);

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

    void add(Missile missile) {
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
        this.missiles = new CopyOnWriteArrayList<>();
        this.explosions = new ArrayList<>();
        this.walls = Arrays.asList(
                new Wall(200, 140, true, 15),
                new Wall(200, 540, true, 15),
                new Wall(100, 160, false, 12),
                new Wall(700, 160, false, 12)
        );
        this.initEnemyTanks();
        this.setPreferredSize(new Dimension(800,600));
    }


    //How to solve concurrent modification problem
    // 1. change ArrayList<> to CopyOnWriteArrayList
    private void initEnemyTanks() {
        this.enemyTanks = new CopyOnWriteArrayList<>();
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
        if (!playerTank.isLive()) {
            g.setColor(Color.RED);
            g.setFont(new Font(null, Font.BOLD, 100));
            g.drawString("Game Over", 100, 200);
            g.setFont(new Font(null, Font.BOLD, 60));
            g.drawString("Press F2 to restart", 100, 360);
        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font(null, Font.BOLD, 16));
            g.drawString("Missiles: " + missiles.size(), 10, 50);
            g.drawString("Explosions: " + explosions.size(), 10, 70);
            g.drawString("Player Tank HP: " + playerTank.getHp(), 10, 90);
            g.drawString("Enemy Left: " + enemyTanks.size(), 10, 110);
            g.drawString("Enemy Killed: " + enemyKilled.get(), 10, 130);

            playerTank.draw(g);

            int count = enemyTanks.size();
            enemyTanks.removeIf(t -> !t.isLive());
            enemyKilled.addAndGet(count - enemyTanks.size());


            if (enemyTanks.isEmpty()) {
                this.initEnemyTanks();
            }
            for (Tank tank : enemyTanks) {
                tank.draw(g);
            }
            for (Wall wall : walls) {
                wall.draw(g);
            }
            //System.out.println(Thread.currentThread().getName());
            missiles.removeIf(m -> !m.isLive());
            for (Missile missile : missiles) {
                missile.draw(g);
            }
            explosions.removeIf(e -> !e.isLive());
            for (Explosion explosion : explosions) {
                explosion.draw(g);
            }
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
            try{
                client.repaint();
                if (client.playerTank.isLive()) {
                //System.out.println(Thread.currentThread().getName());
                    for (Tank tank : client.enemyTanks) {
                        tank.actRandomly();
                    }
                }
                Thread.sleep(40);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void restart() {
        if (!playerTank.isLive()) {
            this.playerTank = new Tank(400, 100, Direction.LEFT);
            //playerTank.setLive(true);
            //playerTank.setHp(100);
        }
        this.initEnemyTanks();
    }
}

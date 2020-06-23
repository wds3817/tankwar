package com.javawds.tankwar;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;

class Tank {
    private int x;
    private int y;
    private Direction direction;
    private boolean enemy;


     Tank(int x, int y, Direction direction) {
        this(x, y, false, direction);
    }

     Tank(int x, int y, boolean enemy, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.enemy = enemy;
    }

     private void move() {
        if (this.stopped) return;
        switch (direction) {
            case UP:
                y-=10;
                break;
            case DOWN:
                y+=10;
                break;
            case LEFT:
                x-=10;
                break;
            case RIGHT:
                x+=10;
                break;
            case LEFT_UP:
                x-=10;
                y-=10;
                break;
            case RIGHT_UP:
                x+=10;
                y-=10;
                break;
            case LEFT_DOWN:
                x-=10;
                y+=10;
                break;
            case RIGHT_DOWN:
                x+=10;
                y+=10;
                break;
        }
        //return null;
    }

    Image getImage() {
        String prefix = enemy ? "e" : "";
        return direction.getImage(prefix + "tank");
//        switch (direction) {
//            case UP: return Tools.getImage(prefix + "tankU.gif");
//            case DOWN: return Tools.getImage(prefix + "tankD.gif");
//            case LEFT: return Tools.getImage(prefix + "tankL.gif");
//            case RIGHT: return Tools.getImage(prefix + "tankR.gif");
//            case UPLEFT: return Tools.getImage(prefix +  "tankLU.gif");
//            case UPRIGHT: return Tools.getImage(prefix +  "tankRU.gif");
//            case DOWNLEFT: return Tools.getImage(prefix + "tankLD.gif");
//            case DOWNRIGHT: return Tools.getImage(prefix +  "tankRD.gif");
//        }
//        return null;
    }

    void draw(Graphics g) {
        int oldx = x, oldy = y;
        this.determineDirection();
        this.move();

        if (x < 0) {
            x = 0;
        } else if (x > 800 - getImage().getWidth(null)) {
            x = 800 - getImage().getWidth(null);
        }
        if (y < 0) {
            y = 0;
        } else if (y > 600 - getImage().getHeight(null)) {
            y = 600 - getImage().getHeight(null);
        }
        Rectangle rec = this.getRectangle();
        for (Wall wall : GameClient.getInstance().getWalls()) {
            if (rec.intersects(wall.getRectangle())) {
                x = oldx;
                y = oldy;
                break;
                //return;
            }
        }

        for (Tank enemyTank : GameClient.getInstance().getEnemyTanks()) {
            if (rec.intersects(enemyTank.getRectangle())) {
                x = oldx;
                y = oldy;
                break;
            }
        }
        g.drawImage(this.getImage(), this.x, this.y, null);
    }

     private Rectangle getRectangle() {
        return new Rectangle(x, y, getImage().getWidth(null), getImage().getHeight(null));
    }

    private boolean up, down, left, right;

     void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                up = true;
                break;
            case KeyEvent.VK_DOWN:
                down = true;
                break;
            case KeyEvent.VK_LEFT:
                left = true;
                break;
            case KeyEvent.VK_RIGHT:
                right = true;
                break;
            case KeyEvent.VK_CONTROL:
                fire();
                break;
            case KeyEvent.VK_A:
                superFire();
                break;
        }
        this.determineDirection();
    }

    private void fire() {
         Missile missile = new Missile(x + getImage().getWidth(null) / 2 - 5,
                 y + getImage().getHeight(null) / 2 - 5, enemy, direction);
         GameClient.getInstance().getMissiles().add(missile);
        playAudio("shoot.wav");
    }

    private void superFire() {
         for (Direction direction : Direction.values()) {
             Missile missile = new Missile(x + getImage().getWidth(null) / 2 - 5,
                     y + getImage().getHeight(null) / 2 - 5, enemy, direction);
             GameClient.getInstance().getMissiles().add(missile);
         }
        String audioFile = new Random().nextBoolean() ? "supershoot.aiff" : "supershoot.wav";
        playAudio(audioFile);
    }

    private void playAudio(String fileName) {
        Media sound = new Media(new File("assets/audios/" + fileName).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }


    private boolean stopped;

    private void determineDirection() {
        if (!up && !left && !down && !right) {
            this.stopped = true;
        } else {
            if (up && !left && !down && !right) {
                this.direction = Direction.UP;
            } else if (!up && !left && down && !right) {
                this.direction = Direction.DOWN;
            } else if (!up && left && !down && !right) {
                this.direction = Direction.LEFT;
            } else if (up && left && !down && !right) {
                this.direction = Direction.LEFT_UP;
            } else if (up && !left && !down && right) {
                this.direction = Direction.RIGHT_UP;
            } else if (!up && left && down && !right) {
                this.direction = Direction.LEFT_DOWN;
            } else if (!up && !left && down && right) {
                this.direction = Direction.RIGHT_DOWN;
            } else if (!up && !left && !down && right) {
                this.direction = Direction.RIGHT;
            }
            this.stopped = false;
        }
    }

     void keyRelease(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                up = false;
                break;
            case KeyEvent.VK_DOWN:
                down = false;
                break;
            case KeyEvent.VK_LEFT:
                left = false;
                break;
            case KeyEvent.VK_RIGHT:
                right = false;
                break;
        }
        //this.determineDirection();
    }
}

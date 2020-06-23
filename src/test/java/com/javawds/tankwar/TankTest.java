package com.javawds.tankwar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TankTest {

    @Test
    void getImage() {
        for (Direction direction : Direction.values()) {
            Tank tank = new Tank(0, 0, false, direction);
            assertTrue(tank.getImage().getWidth(null) > 0, direction + " cannot get valid image");
            Tank enemyTank = new Tank(0, 0, true, direction);
            assertTrue(enemyTank.getImage().getWidth(null) > 0, direction +  "cannot get valide image");
//            Tank tank = new Tank(0, 0, false, direction);
//            assertNotNull(tank.getImage());
//
//            Tank enemyTank = new Tank(0,0, true, direction);
//            assertNotNull(tank.getImage());
        }
    }
}
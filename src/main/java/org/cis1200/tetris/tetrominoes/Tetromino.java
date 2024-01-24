package org.cis1200.tetris.tetrominoes;

import org.cis1200.tetris.GameCourt;

public class Tetromino {
    // center coordinates
    private int x;
    private int y;
    private final int color;

    // general properties
    private int v; // velocity: either 0 or 1 (not moving / moving per tick)
    private int dir;

    public Tetromino(int color) {
        this.y = GameCourt.TET_SPAWN_Y;
        this.x = GameCourt.TET_SPAWN_X;
        this.v = 0;
        this.dir = 0;
        this.color = color;
    }

    // reset Tetromino to be used again
    public void reset() {
        this.y = GameCourt.TET_SPAWN_Y;
        this.x = GameCourt.TET_SPAWN_X;
        this.dir = 0;
        v = 0; // just to be safe: set v to 0
    }

    // generic checkIfLose (to be overwritten)
    public boolean checkIfLose(int[][] grid) {
        return false;
    }

    // universal check lose condition
    public boolean checkIfLose(int[][] grid, OrientationCoords curCoords) {
        // if not collide, not lost, if collide, then lost
        return (this.clipCollision(grid, curCoords, GameCourt.TET_SPAWN_Y, GameCourt.TET_SPAWN_X));
    }

    // generic renders (to be overwritten)
    public void render(int[][] grid) {
    }

    public void render(int[][] grid, int color) {
    }

    // universal render in the grid
    public void render(int[][] grid, OrientationCoords curCoords, int color) {
        for (int i = 0; i < 4; i++) { // 4 blocks per piece
            int y = curCoords.gy(i) + this.getY();
            int x = curCoords.gx(i) + this.getX();
            grid[y][x] = color;
        }
    }

    // clear prev render (to be overwritten)
    public void delPrevRender(int[][] grid) {
    }

    // helper clip function with any x and y
    public int[] clip(OrientationCoords dirCoords, int y, int x) {
        int[] retClip = new int[2]; // (y, x)

        for (int i = 0; i < 4; i++) { // hardcode checking 4 blocks
            int blockY = dirCoords.gy(i) + y;
            int blockX = dirCoords.gx(i) + x;
            if (blockY < 0) {
                retClip[0] = -blockY; // if -2 -> 2
            } else if (blockY > 19) { // hard coded maximum Y
                retClip[0] = 19 - blockY; // if over by 2 -> -2
            }

            if (blockX < 0) {
                retClip[1] = -blockX;
            }
            if (blockX > 9) { // hard coded maximum X
                retClip[1] = 9 - blockX;
            }
        }
        return retClip;
    }

    // generic collision check (true = colliding, false = not)
    public boolean clipCollision(int[][] grid, OrientationCoords dirCoords, int y, int x) {
        for (int i = 0; i < 4; i++) { // hardcode checking all 4 blocks
            int newY = dirCoords.gy(i) + y;
            int newX = dirCoords.gx(i) + x;
            if (newY < 0 || newY > 19) {
                return true;
            } else if (newX < 0 || newX > 9) {
                return true;
            } else if (grid[newY][newX] != 0) {
                return true;
            }
        }
        return false;
    }

    // helper function for rotation
    public int nextRotate(int dir) {
        dir++;
        if (dir >= 4) {
            dir = 0;
        }
        return dir;
    }

    // standard move left, right (to be overwritten)
    public void left(int[][] grid) {
    }

    public void right(int[][] grid) {
    }

    // standard move downs (to be overwritten)
    public boolean down(int[][] grid) {
        return true;
    }

    public boolean tickVertical(int[][] grid) {
        return true;
    }

    public void fastDown(int[][] grid) {
    }

    // generic rotate method (to be overwritten)
    public void rotate(int[][] grid) {
    };

    // universal rotate method
    public void rotate(int[][] grid, OrientationCoords curCoords) {
        boolean canRotate = true;
        // curCoords is coordinates of rotated block
        int[] offset = clip(curCoords, y, x); // (y, x)

        if (!clipCollision(grid, curCoords, y + offset[0], x + offset[1])) {
            this.dir = nextRotate(this.dir);
            this.y = y + offset[0];
            this.x = x + offset[1];
        }
    }

    // get & update values
    public int getDir() {
        return dir;
    }

    public int getVelocity() {
        return v;
    }

    public void changeVelocity(int newVelocity) {
        v = newVelocity;
    }

    public int getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void offsetX(int x) {
        this.x += x;
    }

    public void offsetY(int y) {
        this.y += y;
    }

    // for fastfalls
    public void setY(int y) {
        this.y = y;
    }

    // for imports
    public void setX(int x) {
        this.x = x;
    }

    public void setDir(int dir) {
        if (dir >= 4 || dir < 0) {
            dir = 0;
        }
        this.dir = dir;
    }
}

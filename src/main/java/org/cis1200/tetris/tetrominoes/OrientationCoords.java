package org.cis1200.tetris.tetrominoes;

public class OrientationCoords {
    // 4 relative coordinates (index -> direction)
    private int[] x;
    private int[] y;
    private int curAdd; // when adding nums to list

    public OrientationCoords() {
        this.x = new int[4]; // hard coded -- each tetromino has 4 blocks
        this.y = new int[4];
        this.curAdd = 0;
    }

    public void addYX(int y, int x) {
        if (curAdd > 4) {
            throw new RuntimeException("Attempted to add >4 blocks");
        }
        this.x[curAdd] = x;
        this.y[curAdd] = y;
        curAdd++;
    }

    public int gx(int i) { // get x
        return this.x[i];
    }

    public int gy(int i) { // get y
        return this.y[i];
    }
}

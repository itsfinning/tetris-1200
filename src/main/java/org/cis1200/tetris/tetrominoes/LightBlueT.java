package org.cis1200.tetris.tetrominoes;

import org.cis1200.tetris.GameCourt;

public class LightBlueT extends Tetromino {
    private final OrientationCoords[] orientations;

    public LightBlueT() {
        super(3);
        // hard coded 4 orientations
        this.orientations = new OrientationCoords[4];

        // init orientations

        // for light blue SPECIFICALLY, center is rel (1,1) or "O"

        // ____
        // xOxx
        // ____
        // ____
        orientations[0] = new OrientationCoords();
        orientations[0].addYX(0, 0);
        orientations[0].addYX(0, -1);
        orientations[0].addYX(0, 1);
        orientations[0].addYX(0, 2);

        // __x_
        // _Oo_
        // __x_
        // __x_
        orientations[1] = new OrientationCoords();
        orientations[1].addYX(0, 1);
        orientations[1].addYX(-1, 1);
        orientations[1].addYX(1, 1);
        orientations[1].addYX(2, 1);

        // ____
        // _O__
        // xxox
        // ____
        orientations[2] = new OrientationCoords();
        orientations[2].addYX(1, -1);
        orientations[2].addYX(1, 0);
        orientations[2].addYX(1, 1);
        orientations[2].addYX(1, 2);

        // _x__
        // _O__
        // _o__
        // _x__
        orientations[3] = new OrientationCoords();
        orientations[3].addYX(0, 0);
        orientations[3].addYX(-1, 0);
        orientations[3].addYX(1, 0);
        orientations[3].addYX(2, 0);
    }

    // generic internal render call
    @Override
    public void render(int[][] grid) {
        this.render(grid, super.getColor());
    }

    // standard check if lose for a child tetromino
    @Override
    public boolean checkIfLose(int[][] grid) {
        OrientationCoords curCoords = orientations[this.getDir()];
        return super.checkIfLose(grid, curCoords);
    }

    // delete previous render before moving x and y
    @Override
    public void delPrevRender(int[][] grid) {
        this.render(grid, 0);
    }

    // main child render method
    @Override
    public void render(int[][] grid, int color) {
        OrientationCoords curCoords = orientations[this.getDir()];
        render(grid, curCoords, color);
    }

    @Override
    // standard move down vertically by velocity
    public boolean tickVertical(int[][] grid) {
        // ret true = object is still active & false ow
        if (super.getVelocity() == 1) {
            return this.down(grid);
        }
        return false;
    }

    @Override
    public boolean down(int[][] grid) {
        // ret true = object is still active & false ow
        OrientationCoords curCoords = orientations[this.getDir()];
        if (super.clipCollision(grid, curCoords, this.getY() + 1, this.getX())) {
            // hit bottom!
            super.changeVelocity(0);
            return false;
        } else {
            super.offsetY(1);
        }
        return true;
    }

    public void fastDown(int[][] grid) {
        OrientationCoords curCoords = orientations[this.getDir()];
        int initY = this.getY();

        for (int i = initY; i <= GameCourt.GRID_HEIGHT; i++) {
            if (super.clipCollision(grid, curCoords, i, this.getX())) {
                if (i == initY) {
                    super.changeVelocity(0);
                    break;
                } else {
                    super.setY(i - 1);
                    super.changeVelocity(0);
                    break;
                }
            }
        }
    }

    @Override
    public void left(int[][] grid) {
        OrientationCoords curCoords = orientations[this.getDir()];
        if (!clipCollision(grid, curCoords, this.getY(), this.getX() - 1)) {
            super.offsetX(-1);
        }
    }

    @Override
    public void right(int[][] grid) {
        OrientationCoords curCoords = orientations[this.getDir()];
        if (!clipCollision(grid, curCoords, this.getY(), this.getX() + 1)) {
            super.offsetX(1);
        }
    }

    // check if can rotate and rotate accordingly
    @Override
    public void rotate(int[][] grid) {
        OrientationCoords curCoords = orientations[this.nextRotate(this.getDir())];
        super.rotate(grid, curCoords);
    }
}

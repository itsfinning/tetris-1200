package org.cis1200.tetris;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A game object displayed using an image.
 *
 * Note that the image is read from the file when the object is constructed, and
 * that all objects created by this constructor share the same image data (i.e.
 * img is static). This is important for efficiency: your program will go very
 * slowly if you try to create a new BufferedImage every time the draw method is
 * invoked.
 */
public class Block extends GameObj {
    public static final String EMPTY_FILE = "files/empty.png";

    // Tetromino blocks
    public static final String DARKBLUE_FILE = "files/darkblue.png";
    public static final String GREEN_FILE = "files/green.png";
    public static final String LIGHTBLUE_FILE = "files/lightblue.png";
    public static final String ORANGE_FIE = "files/orange.png";
    public static final String PURPLE_FILE = "files/purple.png";
    public static final String RED_FILE = "files/red.png";
    public static final String YELLOW_FILE = "files/yellow.png";

    public static final int SIZE = 20;

    private static BufferedImage darkBlueBlock;
    private static BufferedImage greenBlock;
    private static BufferedImage lightBlueBlock;
    private static BufferedImage purpleBlock;
    private static BufferedImage redBlock;
    private static BufferedImage yellowBlock;
    private static BufferedImage orangeBlock;
    private static BufferedImage emptyBlock;

    public Block(int x, int y) {
        super(x, y, SIZE, SIZE);

        try {
            // try to get all blocks
            if (emptyBlock == null) {
                emptyBlock = ImageIO.read(new File(EMPTY_FILE));
            }
            if (darkBlueBlock == null) {
                darkBlueBlock = ImageIO.read(new File(DARKBLUE_FILE));
            }
            if (lightBlueBlock == null) {
                lightBlueBlock = ImageIO.read(new File(LIGHTBLUE_FILE));
            }
            if (greenBlock == null) {
                greenBlock = ImageIO.read(new File(GREEN_FILE));
            }
            if (orangeBlock == null) {
                orangeBlock = ImageIO.read(new File(ORANGE_FIE));
            }
            if (yellowBlock == null) {
                yellowBlock = ImageIO.read(new File(YELLOW_FILE));
            }
            if (redBlock == null) {
                redBlock = ImageIO.read(new File(RED_FILE));
            }
            if (purpleBlock == null) {
                purpleBlock = ImageIO.read(new File(PURPLE_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public void draw(Graphics g) {
        throw new IllegalArgumentException("Pass in a block!");
    }

    public void draw(Graphics g, String block) {
        switch (block) {
            case "yellow" -> g.drawImage(
                    yellowBlock, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null
            );
            case "red" -> g.drawImage(
                    redBlock, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null
            );
            case "lightBlue" -> g.drawImage(
                    lightBlueBlock, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(),
                    null
            );
            case "darkBlue" -> g.drawImage(
                    darkBlueBlock, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(),
                    null
            );
            case "purple" -> g.drawImage(
                    purpleBlock, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null
            );
            case "orange" -> g.drawImage(
                    orangeBlock, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null
            );
            case "green" -> g.drawImage(
                    greenBlock, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null
            );
            default -> g.drawImage(
                    emptyBlock, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null
            );
        }
    }
}

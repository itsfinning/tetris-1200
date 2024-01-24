package org.cis1200.tetris;

import org.cis1200.tetris.tetrominoes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// generate random blocks
import java.io.*;
import java.util.Arrays;
import java.util.Random;

/**
 * GameCourt
 *
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 */
public class GameCourt extends JPanel {
    // standard tetris grid properties
    public final static int GRID_HEIGHT = 20;
    public final static int GRID_WIDTH = 10;
    public final static int TET_SPAWN_Y = 1;
    public final static int TET_SPAWN_X = 4;

    // the game grids
    private final int[][] grid = new int[GRID_HEIGHT][GRID_WIDTH];

    public int[][] getGrid() {
        int[][] copyGrid = new int[GRID_HEIGHT][GRID_WIDTH];
        for (int i = 0; i < GRID_HEIGHT; i++) {
            copyGrid[i] = grid[i].clone(); // 2d array deep-copy
        }
        return copyGrid;
    }

    private final Block[][] blockGrid = new Block[20][10];

    // all available Tetrominoes
    private final Tetromino[] tetrominoes = {
        new DarkBlueT(),
        new LightBlueT(),
        new RedT(),
        new YellowT(),
        new OrangeT(),
        new PurpleT(),
        new GreenT()
    };

    // global random num gen
    Random rand = new Random();
    private int[] nextBlocks = { -1, -1, -1 };
    private int curMovingTet;

    // current block the user is holding (-1 is none)
    private int holdBlock = -1;

    // user score
    private int score = 0;

    public int getScore() {
        return score;
    }

    private boolean playing = false; // whether the game is running
    private final JLabel status; // Current status text, i.e. "Running..."
    private boolean noPressAgain = true;
    private boolean paused = false; // whether game is paused or not

    // whether game is lost or not
    private boolean lostGame = false;

    // Game constants
    public static final int COURT_WIDTH = 450;
    public static final int COURT_HEIGHT = 500;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 50;

    // Update interval for tetris piece to autoDrop
    private int autoDropTimer = 0;

    public GameCourt(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the
        // given INTERVAL. We register an ActionListener with this timer, whose
        // actionPerformed() method is called each time the timer triggers. We
        // define a helper method called tick() that actually does everything
        // that should be done in a single time step.
        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key
        // is pressed, by changing the square's velocity accordingly. (The tick
        // method below actually moves the square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    tetrominoes[curMovingTet].delPrevRender(grid);
                    tetrominoes[curMovingTet].left(grid);
                    tetrominoes[curMovingTet].render(grid);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    tetrominoes[curMovingTet].delPrevRender(grid);
                    tetrominoes[curMovingTet].right(grid);
                    tetrominoes[curMovingTet].render(grid);
                } else if (noPressAgain && e.getKeyCode() == KeyEvent.VK_UP) {
                    noPressAgain = false;
                    rotatePiece();
                } else if (noPressAgain && e.getKeyCode() == KeyEvent.VK_SPACE) {
                    noPressAgain = false;
                    tetrominoes[curMovingTet].delPrevRender(grid);
                    tetrominoes[curMovingTet].fastDown(grid);
                    tetrominoes[curMovingTet].render(grid);
                } else if (noPressAgain && e.getKeyCode() == KeyEvent.VK_C) {
                    noPressAgain = false;
                    int prevCurMovingTet = curMovingTet;
                    if (holdBlock == -1) {
                        tetrominoes[curMovingTet].delPrevRender(grid);
                        tetrominoes[curMovingTet].reset();
                        curMovingTet = popRandomBlock();
                    } else {
                        tetrominoes[curMovingTet].delPrevRender(grid);
                        tetrominoes[curMovingTet].reset();
                        curMovingTet = holdBlock;
                    }
                    tetrominoes[curMovingTet].changeVelocity(1);
                    tetrominoes[curMovingTet].render(grid);

                    holdBlock = prevCurMovingTet;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    tetrominoes[curMovingTet].delPrevRender(grid);
                    tetrominoes[curMovingTet].down(grid);
                    tetrominoes[curMovingTet].render(grid);
                }
            }

            public void keyReleased(KeyEvent e) {
                noPressAgain = true;
            }
        });

        this.status = status;
    }

    public void rotatePiece() {
        tetrominoes[curMovingTet].delPrevRender(grid);
        tetrominoes[curMovingTet].rotate(grid);
        tetrominoes[curMovingTet].render(grid);
    }

    // open instructions panel
    public void instructions() {
        pause();
        // show panel with instructions
        JFrame instructionsFrame = new JFrame("How to Play");
        JPanel instructionsPanel = new JPanel();
        JLabel line1 = new JLabel("Welcome to Tetris!");
        JLabel line2 = new JLabel("Use the LEFT and RIGHT arrows to move the falling Tetromino");
        JLabel line3 = new JLabel(" and use the UP arrow to rotate it.");
        JLabel line4 = new JLabel("Use the DOWN arrow to speed up a block falling, and use the");
        JLabel line5 = new JLabel("SPACEBAR to make a block fast fall.");
        JLabel line6 = new JLabel("To hold a block, press 'c' on the keyboard while it's falling.");
        JLabel line7 = new JLabel(
                "In this version of Tetris, you can also import and export board states"
        );
        JLabel line8 = new JLabel(
                " for practice purposes! To do so, press 'pause' in the menu and press "
        );
        JLabel line9 = new JLabel("'import' or 'export' afterwards.");

        instructionsPanel.add(line1);
        instructionsPanel.add(line2);
        instructionsPanel.add(line3);
        instructionsPanel.add(line4);
        instructionsPanel.add(line5);
        instructionsPanel.add(line6);
        instructionsPanel.add(line7);
        instructionsPanel.add(line8);
        instructionsPanel.add(line9);
        // panel for centering
        instructionsFrame.add(instructionsPanel);
        instructionsFrame.setLocationRelativeTo(null);
        instructionsFrame.setSize(450, 280);
        instructionsFrame.setLocation(400, 200);
        instructionsFrame.setVisible(true);
    }

    // pause game
    public void pause() {
        if (!lostGame) {
            paused = !paused;
            if (paused) {
                playing = false;
                status.setText("Paused.");
            } else {
                playing = true;
                requestFocusInWindow();
                status.setText("Running...");
            }
        }
    }

    public void importState(String path) {
        // IMPORT EXPORTED FORMAT OF:
        // ______ 20x10 BOARD ______
        // \n
        // curBlock_X curBlock_Y curBlock_Color curBlock_Direction
        // \n
        // score
        // \n
        // curHeldBlock
        // \n
        // nextBlock_1 nextBlock_2 nextBlock_3

        if (!paused && !lostGame) {
            requestFocusInWindow();
            return;
        }
        try {
            paused = true; // in case user called after losing game
            tetrominoes[curMovingTet].reset(); // reset all blocks to og

            if (path.isEmpty()) {
                path = "files/save/gameState.tet"; // default path
            }

            BufferedReader b = new BufferedReader(new FileReader(path));
            for (int i = 0; i < GRID_HEIGHT; i++) {
                String curLine = b.readLine().strip();
                for (int j = 0; j < GRID_WIDTH; j++) {
                    grid[i][j] = Character.getNumericValue((curLine.charAt(j)));
                }
            }

            b.readLine(); // get rid of \n

            // [0] = x, [1] = y, [2] = curMovingTet, [3] = dir
            String[] curMoving = b.readLine().strip().split(" ");
            curMovingTet = Integer.parseInt(curMoving[2]);
            tetrominoes[curMovingTet].setX(Integer.parseInt(curMoving[0]));
            tetrominoes[curMovingTet].setY(Integer.parseInt(curMoving[1]));
            tetrominoes[curMovingTet].setDir(Integer.parseInt(curMoving[3]));
            tetrominoes[curMovingTet].render(grid);
            tetrominoes[curMovingTet].changeVelocity(1);

            b.readLine(); // get rid of \n

            score = Integer.parseInt(b.readLine().strip());

            b.readLine(); // get rid of \n

            holdBlock = Integer.parseInt(b.readLine().strip());

            b.readLine(); // get rid of \n

            // [0], [1], [2]
            String[] nextStrings = b.readLine().strip().split(" ");
            nextBlocks[0] = Integer.parseInt(nextStrings[0]);
            nextBlocks[1] = Integer.parseInt(nextStrings[1]);
            nextBlocks[2] = Integer.parseInt(nextStrings[2]);

            repaint(); // repaint to show user changes
            status.setText("Paused."); // in case the user lost and is importing
            lostGame = false;

            // show panel to let user know import was successful
            JFrame successFrame = new JFrame("Success!");
            JPanel successPanel = new JPanel();
            JLabel successLabel = new JLabel("Successfully imported from " + path);
            successPanel.add(successLabel);
            // panel for centering
            successFrame.add(successPanel);
            successFrame.setLocationRelativeTo(null);
            successFrame.setSize(400, 75);
            successFrame.setLocation(400, 200);
            successFrame.setVisible(true);
        } catch (IOException e) {
            JFrame failureFrame = new JFrame("Invalid Input File");
            JPanel failurePanel = new JPanel();
            JLabel failureLabel = new JLabel("Input file not found at: " + path);
            failurePanel.add(failureLabel);
            // panel for centering
            failureFrame.add(failurePanel);
            failureFrame.setLocationRelativeTo(null);
            failureFrame.setSize(400, 75);
            failureFrame.setLocation(400, 200);
            failureFrame.setVisible(true);
        } catch (Exception e) {
            JFrame failureFrame = new JFrame("Bad Input File");
            JPanel failurePanel = new JPanel();
            JLabel failureLabel = new JLabel("Bad input file at: " + path);
            failurePanel.add(failureLabel);
            // panel for centering
            failureFrame.add(failurePanel);
            failureFrame.setLocationRelativeTo(null);
            failureFrame.setSize(400, 75);
            failureFrame.setLocation(400, 200);
            failureFrame.setVisible(true);
        }
    }

    public String getBoardState() {
        String retStr = "";
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                retStr += grid[i][j];
            }
            retStr += "\n";
        }
        return retStr;
    }

    public void exportState(String path) {
        if (!paused || lostGame) {
            requestFocusInWindow();
            return;
        }
        try {
            // clear this and spawn upon import and after exporting grid
            tetrominoes[curMovingTet].delPrevRender(grid);

            if (path.isEmpty()) {
                path = "files/save/gameState.tet"; // default path
            }

            Writer w = new FileWriter(path);
            w.write(getBoardState());
            w.write("\n"); // enter before outputting x & y
            String coords = tetrominoes[curMovingTet].getX() + " " +
                    tetrominoes[curMovingTet].getY() + " " +
                    curMovingTet + " " +
                    tetrominoes[curMovingTet].getDir();
            w.write(coords);
            w.write("\n\n"); // enter before outputting score
            w.write("" + score);
            w.write("\n\n"); // enter before outputting held block
            w.write("" + holdBlock);
            w.write("\n\n"); // enter before outputting next 3 blocks
            w.write(nextBlocks[0] + " " + nextBlocks[1] + " " + nextBlocks[2]);
            w.flush();
            w.close();

            tetrominoes[curMovingTet].render(grid); // re-render block

            // show panel to let user know export was successful
            JFrame successFrame = new JFrame("Success!");
            JPanel successPanel = new JPanel();
            JLabel successLabel = new JLabel("Successfully exported to " + path);
            successPanel.add(successLabel);
            // panel for centering
            successFrame.add(successPanel);
            successFrame.setLocationRelativeTo(null);
            successFrame.setSize(400, 75);
            successFrame.setLocation(400, 200);
            successFrame.setVisible(true);
        } catch (IOException e) {
            // show panel to let user know export was successful
            JFrame failureFrame = new JFrame("Failure");
            JPanel failurePanel = new JPanel();
            JLabel failureLabel = new JLabel("Failed to write to " + path);
            failurePanel.add(failureLabel);
            // panel for centering
            failureFrame.add(failurePanel);
            failureFrame.setLocationRelativeTo(null);
            failureFrame.setSize(400, 75);
            failureFrame.setLocation(400, 200);
            failureFrame.setVisible(true);
        }
    }

    public void resetGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = 0;
            }
        }
    }

    public void clearLinesHelper(int start, int end) {
        // start and end are inclusive fields
        int offset = end - start + 1;

        // update scoring
        if (offset == 4) {
            score += 800;
        } else if (offset == 3) {
            score += 500;
        } else if (offset == 2) {
            score += 300;
        } else {
            score += 100;
        }

        for (int i = end; i >= 0; i--) {
            if (i - offset < 0) {
                grid[i] = new int[GRID_WIDTH];
            } else {
                if (!Arrays.equals(grid[i], grid[i - offset])) { // empty optimization
                    grid[i] = grid[i - offset].clone();
                    grid[i - offset] = new int[GRID_WIDTH];
                }
            }
        }
    }

    public void clearLines() {
        int startClear = -1;
        for (int i = 0; i <= grid.length; i++) {
            if (i == grid.length) {
                if (startClear != -1) {
                    clearLinesHelper(startClear, grid.length - 1);
                    return;
                } else {
                    return;
                }
            }

            boolean isClearLine = true;
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 0) {
                    isClearLine = false;
                    break;
                }
            }
            if (isClearLine && startClear == -1) {
                startClear = i;
            } else if (!isClearLine && startClear != -1) {
                clearLinesHelper(startClear, i - 1);
                return;
            }
        }
    }

    public void initBlockGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                blockGrid[i][j] = new Block(30 + 20 * j, 40 + 20 * i);
            }
        }
    }

    public void renderBlockGrid(Graphics g) { // only call after updating block grid
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                switch (grid[i][j]) {
                    case 2 -> blockGrid[i][j].draw(g, "darkBlue");
                    case 3 -> blockGrid[i][j].draw(g, "lightBlue");
                    case 4 -> blockGrid[i][j].draw(g, "red");
                    case 5 -> blockGrid[i][j].draw(g, "yellow");
                    case 6 -> blockGrid[i][j].draw(g, "orange");
                    case 7 -> blockGrid[i][j].draw(g, "purple");
                    case 8 -> blockGrid[i][j].draw(g, "green");
                    default -> blockGrid[i][j].draw(g, "empty");

                }
            }
        }
    }

    public int popRandomBlock() { // pop and update random block
        int retBlock = rand.nextInt(tetrominoes.length);
        if (nextBlocks[0] == -1) { // init list
            nextBlocks[0] = rand.nextInt(tetrominoes.length);
            nextBlocks[1] = rand.nextInt(tetrominoes.length);
            nextBlocks[2] = rand.nextInt(tetrominoes.length);
        } else {
            retBlock = nextBlocks[0];
            nextBlocks[0] = nextBlocks[1];
            nextBlocks[1] = nextBlocks[2];
            nextBlocks[2] = rand.nextInt(tetrominoes.length);
        }
        return retBlock;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        initBlockGrid();
        resetGrid();
        lostGame = false; // haven't lost game upon new game

        // reset nextBlocks to force new blocks to gen
        nextBlocks = new int[] { -1, -1, -1 };
        curMovingTet = popRandomBlock();
        tetrominoes[curMovingTet].reset();
        tetrominoes[curMovingTet].changeVelocity(1);
        tetrominoes[curMovingTet].render(grid);

        playing = true;
        status.setText("Running...");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing && !lostGame) {
            // update the display
            repaint();
            autoDropTimer += 50;
            if (tetrominoes[curMovingTet].getVelocity() == 0) {
                tetrominoes[curMovingTet].reset();
                clearLines();

                // generate new block
                curMovingTet = popRandomBlock();
                tetrominoes[curMovingTet].changeVelocity(1);
                if (tetrominoes[curMovingTet].checkIfLose(grid)) {
                    playing = false;
                    lostGame = true;
                } else {
                    tetrominoes[curMovingTet].render(grid);
                }
            }
        } else if (lostGame) {
            status.setText("You lose!");
        }
    }

    public void renderMiscBlock(Graphics g, int block, int x, int y) {
        // x and y are center coordinates
        OrientationCoords curOrientation = new OrientationCoords();
        String color = "";
        // cases go off tetrominoes list as defined above
        switch (block) {
            case 0 -> {
                color = "darkBlue";
                curOrientation.addYX(0, 0);
                curOrientation.addYX(0, -1);
                curOrientation.addYX(0, 1);
                curOrientation.addYX(-1, -1);
            }
            case 1 -> {
                color = "lightBlue";
                // center by changing strange x offset
                x -= 10;
                y -= 10;
                curOrientation.addYX(0, 0);
                curOrientation.addYX(0, -1);
                curOrientation.addYX(0, 1);
                curOrientation.addYX(0, 2);
            }
            case 2 -> {
                color = "red";
                curOrientation.addYX(0, 0);
                curOrientation.addYX(-1, 0);
                curOrientation.addYX(-1, -1);
                curOrientation.addYX(0, 1);
            }
            case 3 -> {
                color = "yellow";
                // center by changing two x offset
                x -= 10;
                curOrientation.addYX(0, 0);
                curOrientation.addYX(0, 1);
                curOrientation.addYX(-1, 0);
                curOrientation.addYX(-1, 1);
            }
            case 4 -> {
                color = "orange";
                curOrientation.addYX(0, 0);
                curOrientation.addYX(0, -1);
                curOrientation.addYX(0, 1);
                curOrientation.addYX(-1, 1);
            }
            case 5 -> {
                color = "purple";
                curOrientation.addYX(0, 0);
                curOrientation.addYX(0, -1);
                curOrientation.addYX(0, 1);
                curOrientation.addYX(-1, 0);
            }
            case 6 -> {
                color = "green";
                curOrientation.addYX(0, 0);
                curOrientation.addYX(-1, 0);
                curOrientation.addYX(-1, 1);
                curOrientation.addYX(0, -1);
            }
            default -> {
                color = "empty";
                curOrientation.addYX(0, 0);
                curOrientation.addYX(0, 0);
                curOrientation.addYX(0, 0);
                curOrientation.addYX(0, 0);
            }
        }

        for (int i = 0; i < 4; i++) {
            int curX = x + 20 * curOrientation.gx(i);
            int curY = y + 20 * curOrientation.gy(i);
            Block newBlock = new Block(curX, curY);
            newBlock.draw(g, color);
        }
    }

    public void drawNextBar(Graphics g) {
        // {x, y, width, height}
        int[] rectProperties = { 262, 170, 155, 235 };
        g.drawRect(rectProperties[0], rectProperties[1], rectProperties[2], rectProperties[3]);
        g.setColor(new Color(232, 232, 232));
        g.fillRect(rectProperties[0], rectProperties[1], rectProperties[2], rectProperties[3]);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Calibri", Font.BOLD, 25));
        g.drawString("NEXT", 313, 200);

        int baseX = 325;
        int baseY = 240;

        renderMiscBlock(g, nextBlocks[0], baseX, baseY);
        renderMiscBlock(g, nextBlocks[1], baseX, baseY + 55);
        renderMiscBlock(g, nextBlocks[2], baseX, baseY + 110);

        // draw blocks inside

    }

    public void drawHoldBar(Graphics g) {
        // {x, y, width, height}
        int[] rectProperties = { 275, 40, 130, 110 };
        g.drawRect(rectProperties[0], rectProperties[1], rectProperties[2], rectProperties[3]);
        g.setColor(new Color(232, 232, 232));
        g.fillRect(rectProperties[0], rectProperties[1], rectProperties[2], rectProperties[3]);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Calibri", Font.BOLD, 20));
        g.drawString("HOLD", 316, 65);

        // draw blocks inside
        if (holdBlock >= 0 && holdBlock < tetrominoes.length) {
            renderMiscBlock(g, holdBlock, 330, 100);
        }
    }

    private void drawScore(Graphics g) {
        // {x, y, width, height}
        int[] rectProperties = { 275, 425, 130, 40 };
        g.drawRect(rectProperties[0], rectProperties[1], rectProperties[2], rectProperties[3]);
        g.setColor(new Color(232, 232, 232));
        g.fillRect(rectProperties[0], rectProperties[1], rectProperties[2], rectProperties[3]);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Calibri", Font.BOLD, 18));
        g.drawString("SCORE: " + score, 295, 450);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (autoDropTimer >= 850) {
            tetrominoes[curMovingTet].delPrevRender(grid);
            tetrominoes[curMovingTet].tickVertical(grid);
            tetrominoes[curMovingTet].render(grid);
            autoDropTimer = 0;
        }
        drawHoldBar(g);
        drawNextBar(g);
        drawScore(g);
        renderBlockGrid(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}
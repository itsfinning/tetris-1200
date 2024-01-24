package org.cis1200.tetris;

import org.junit.jupiter.api.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class GameTest {
    @Test
    public void standardImportTest() {
        final JLabel status = new JLabel("Running...");
        final GameCourt court = new GameCourt(status);
        court.reset();
        court.pause(); // allow imports

        court.importState(
                "src/test/java/org/cis1200/tetris/boards/gameState.tet"
        );
        // make sure the grid updated from 0 to the numbers in file
        assertEquals(6, court.getGrid()[19][0]);
    }

    @Test
    public void scoreImportTest() {
        final JLabel status = new JLabel("Running...");
        final GameCourt court = new GameCourt(status);
        court.reset();
        court.pause(); // allow imports

        court.importState(
                "src/test/java/org/cis1200/tetris/boards/gameState.tet"
        );
        // did court update to ridiculous score?
        assertEquals(9999999, court.getScore());
    }

    @Test
    public void importIONotFound() {
        final JLabel status = new JLabel("Running...");
        final GameCourt court = new GameCourt(status);
        court.reset();
        court.pause(); // allow imports

        assertDoesNotThrow(
                () -> court.importState(
                        "src/test/java/org/cis1200/tetris/boards/gameState2.tet"
                )
        );
    }

    @Test
    public void importStateCrash() {
        final JLabel status = new JLabel("Running...");
        final GameCourt court = new GameCourt(status);
        court.reset();
        court.pause(); // allow imports

        // make sure the grid updated from 0 to all the numbers
        assertDoesNotThrow(
                () -> court.importState(
                        "src/test/java/org/cis1200/tetris/boards/gameStateCrash.tet"
                )
        );
    }

    @Test
    public void lightBlueTRotate() { // most complicated push back in bounds case
        final JLabel status = new JLabel("Running...");
        final GameCourt court = new GameCourt(status);
        court.reset();
        court.pause(); // allow imports

        court.importState(
                "src/test/java/org/cis1200/tetris/boards/rotateCheckLightBlue.tet"
        );

        int color = 3; // light blue color = 3

        // check no exception is thrown
        assertDoesNotThrow(() -> {
            court.rotatePiece();
        }
        );

        // check if block is where it's supposed to be
        int[][] grid = court.getGrid();
        assertEquals(color, grid[4][6]);
        assertEquals(color, grid[4][7]);
        assertEquals(color, grid[4][8]);
        assertEquals(color, grid[4][9]);
    }

    @Test
    public void redTRotate() { // another push back in bounds case
        final JLabel status = new JLabel("Running...");
        final GameCourt court = new GameCourt(status);
        court.reset();
        court.pause(); // allow imports

        court.importState(
                "src/test/java/org/cis1200/tetris/boards/rotateCheckRed.tet"
        );

        int color = 4; // red color

        // check no exception is thrown
        assertDoesNotThrow(() -> {
            court.rotatePiece();
        }
        );

        // check if block is where it's supposed to be
        int[][] grid = court.getGrid();
        assertEquals(color, grid[8][7]);
        assertEquals(color, grid[8][8]);
        assertEquals(color, grid[9][8]);
        assertEquals(color, grid[9][9]);
    }

    @Test
    public void yellowTRotate() { // rotation does nothing test
        final JLabel status = new JLabel("Running...");
        final GameCourt court = new GameCourt(status);
        court.reset();
        court.pause(); // allow imports

        court.importState(
                "src/test/java/org/cis1200/tetris/boards/rotateCheckYellow.tet"
        );

        int color = 5;

        // check initial position is there
        int[][] grid = court.getGrid();
        assertEquals(color, grid[11][6]);
        assertEquals(color, grid[11][7]);
        assertEquals(color, grid[12][6]);
        assertEquals(color, grid[12][7]);

        // check no exception is thrown
        assertDoesNotThrow(() -> {
            court.rotatePiece();
        }
        );

        // check if yellow block is still in same place
        grid = court.getGrid();
        assertEquals(color, grid[11][6]);
        assertEquals(color, grid[11][7]);
        assertEquals(color, grid[12][6]);
        assertEquals(color, grid[12][7]);
    }

    @Test
    public void greenTRotate() { // another push back in bounds case
        final JLabel status = new JLabel("Running...");
        final GameCourt court = new GameCourt(status);
        court.reset();
        court.pause(); // allow imports

        court.importState(
                "src/test/java/org/cis1200/tetris/boards/rotateCheckGreen.tet"
        );

        int color = 8; // green color

        // check no exception is thrown
        assertDoesNotThrow(() -> {
            court.rotatePiece();
        }
        );

        // check if block is where it's supposed to be
        int[][] grid = court.getGrid();
        assertEquals(color, grid[8][8]);
        assertEquals(color, grid[8][9]);
        assertEquals(color, grid[9][7]);
        assertEquals(color, grid[9][8]);
    }

    @Test
    public void purpleTRotate() { // another push back in bounds case
        final JLabel status = new JLabel("Running...");
        final GameCourt court = new GameCourt(status);
        court.reset();
        court.pause(); // allow imports

        court.importState(
                "src/test/java/org/cis1200/tetris/boards/rotateCheckPurple.tet"
        );

        int color = 7; // purple color

        // check no exception is thrown
        assertDoesNotThrow(() -> {
            court.rotatePiece();
        }
        );

        // check if block is where it's supposed to be
        int[][] grid = court.getGrid();
        assertEquals(color, grid[8][8]);
        assertEquals(color, grid[9][9]);
        assertEquals(color, grid[9][7]);
        assertEquals(color, grid[9][8]);
    }

    @Test
    public void darkBlueTRotate() { // another push back in bounds case
        final JLabel status = new JLabel("Running...");
        final GameCourt court = new GameCourt(status);
        court.reset();
        court.pause(); // allow imports

        court.importState(
                "src/test/java/org/cis1200/tetris/boards/rotateCheckDarkBlue.tet"
        );

        int color = 2; // dark blue color

        // check no exception is thrown
        assertDoesNotThrow(() -> {
            court.rotatePiece();
        }
        );

        // check if block is where it's supposed to be
        int[][] grid = court.getGrid();
        assertEquals(color, grid[8][7]);
        assertEquals(color, grid[8][8]);
        assertEquals(color, grid[8][9]);
        assertEquals(color, grid[9][9]);
    }

    @Test
    public void orangeTRotate() { // another push back in bounds case
        final JLabel status = new JLabel("Running...");
        final GameCourt court = new GameCourt(status);
        court.reset();
        court.pause(); // allow imports

        court.importState(
                "src/test/java/org/cis1200/tetris/boards/rotateCheckOrange.tet"
        );

        int color = 6; // orange color

        // check no exception is thrown
        assertDoesNotThrow(() -> {
            court.rotatePiece();
        }
        );

        // check if block is where it's supposed to be
        int[][] grid = court.getGrid();
        assertEquals(color, grid[3][7]);
        assertEquals(color, grid[3][8]);
        assertEquals(color, grid[3][9]);
        assertEquals(color, grid[4][7]);
    }

    @Test
    public void importAndExportSameBoard() {
        final JLabel status = new JLabel("Running...");
        final GameCourt court = new GameCourt(status);
        court.reset();
        court.pause(); // allow imports

        court.importState(
                "src/test/java/org/cis1200/tetris/boards/gameState.tet"
        );

        court.exportState(
                "src/test/java/org/cis1200/tetris/boards/outputState.tet"
        );

        File file1 = new File("src/test/java/org/cis1200/tetris/boards/gameState.tet");
        File file2 = new File("src/test/java/org/cis1200/tetris/boards/outputState.tet");
        try {
            byte[] f1 = Files.readAllBytes(file1.toPath());
            byte[] f2 = Files.readAllBytes(file2.toPath());
            assertTrue(Arrays.equals(f1, f2));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}

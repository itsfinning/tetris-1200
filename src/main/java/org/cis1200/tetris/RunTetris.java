package org.cis1200.tetris;

// imports necessary libraries for Java swing

import javax.swing.*;
import java.awt.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class RunTetris implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for
        // local variables.

        // Top-level frame in which game components live.
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Tetris");
        frame.setLocation(450, 100);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton instructions = new JButton("How to Play");
        instructions.addActionListener(e -> court.instructions());

        final JButton reset = new JButton("New Game");
        reset.addActionListener(e -> court.reset());

        final JButton pause = new JButton("Pause");
        pause.addActionListener(e -> court.pause());

        final JButton importState = new JButton("Import");
        importState.addActionListener(e -> court.importState(""));

        final JButton exportState = new JButton("Export");
        exportState.addActionListener(e -> court.exportState(""));

        control_panel.add(instructions);
        control_panel.add(reset);
        control_panel.add(pause);
        control_panel.add(importState);
        control_panel.add(exportState);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }
}
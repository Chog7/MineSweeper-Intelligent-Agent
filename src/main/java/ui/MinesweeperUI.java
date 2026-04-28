package ui;

import agent.IntelligentAgent;
import game.Board;
import game.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class MinesweeperUI extends JFrame {
    private final Board board;
    private final IntelligentAgent agent;
    private final JButton[][] buttons;
    private final JLabel statusLabel;
    private long startTime;
    private boolean gameFinished = false;

    public MinesweeperUI(Board board, IntelligentAgent agent) {
        this.board = board;
        this.agent = agent;
        this.buttons = new JButton[board.getWidth()][board.getHeight()];
        this.statusLabel = new JLabel("Status: Ready", SwingConstants.CENTER);
        this.startTime = System.currentTimeMillis();

        initUI();
    }

    private void initUI() {
        setTitle("MineSweeper Intelligent Agent");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(board.getHeight(), board.getWidth()));
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                JButton btn = new JButton("");
                btn.setPreferredSize(new Dimension(40, 40));
                btn.setMargin(new Insets(0, 0, 0, 0));
                btn.setFont(new Font("Arial", Font.BOLD, 14));
                btn.setFocusPainted(false);
                buttons[x][y] = btn;
                gridPanel.add(btn);
            }
        }

        JPanel controlPanel = new JPanel();
        JButton stepButton = new JButton("Step Agent");
        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String action = agent.step();
                statusLabel.setText("Status: " + action);
                updateBoard();
                
                if (board.isGameOver() && !gameFinished) {
                    gameFinished = true;
                    statusLabel.setText("Status: GAME OVER - Hit a mine!");
                    stepButton.setEnabled(false);
                    exportResult();
                } else if (board.isGameWon() && !gameFinished) {
                    gameFinished = true;
                    statusLabel.setText("Status: VICTORY - Board solved!");
                    stepButton.setEnabled(false);
                    exportResult();
                }
            }
        });
        
        controlPanel.add(stepButton);

        add(statusLabel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        updateBoard();
    }

    private void updateBoard() {
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                Tile t = board.getTile(x, y);
                JButton btn = buttons[x][y];

                if (t.isRevealed()) {
                    btn.setEnabled(false);
                    if (t.isMine()) {
                        btn.setText("*");
                        btn.setBackground(Color.RED);
                    } else if (t.getAdjacentMines() > 0) {
                        btn.setText(String.valueOf(t.getAdjacentMines()));
                        btn.setBackground(Color.WHITE);
                    } else {
                        btn.setText("");
                        btn.setBackground(Color.LIGHT_GRAY);
                    }
                } else if (t.isFlagged()) {
                    btn.setText("F");
                    btn.setForeground(Color.RED);
                    btn.setBackground(Color.YELLOW);
                } else {
                    btn.setText("");
                    btn.setBackground(UIManager.getColor("Button.background"));
                }
            }
        }
    }

    public void display() {
        setVisible(true);
    }

    private void exportResult() {
        long timeMs = System.currentTimeMillis() - startTime;
        String csvFile = "manual_results.csv";
        boolean fileExists = new File(csvFile).exists();
        
        try (FileWriter out = new FileWriter(csvFile, StandardCharsets.UTF_8, true);
             CSVPrinter printer = new CSVPrinter(out, fileExists ? CSVFormat.DEFAULT : CSVFormat.Builder.create(CSVFormat.DEFAULT).setHeader(
                     "Difficulty", "Width", "Height", "Mines", "Result", "Completion_%",
                     "TimeMs", "CSP_Moves", "Probabilistic_Guesses").build())) {
            
            String result = board.isGameWon() ? "Win" : "Loss";
            
            double completionPercent = 100.0;
            if (!board.isGameWon()) {
                int totalSafe = (board.getWidth() * board.getHeight()) - board.getNumMines();
                int revealedSafe = totalSafe - board.getUnrevealedSafeTiles();
                completionPercent = ((double) revealedSafe / totalSafe) * 100.0;
            }

            printer.printRecord(
                    "Manual", board.getWidth(), board.getHeight(), board.getNumMines(), result, String.format("%.2f", completionPercent),
                    timeMs, agent.getCspMovesMade(), agent.getProbabilisticGuessesMade()
            );
            System.out.println("Result saved to " + csvFile);
        } catch (IOException ex) {
            System.err.println("Error writing to CSV: " + ex.getMessage());
        }
    }
}

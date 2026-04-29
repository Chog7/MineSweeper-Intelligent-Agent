//Author - Michael (Chog7)

package benchmark;

import agent.IntelligentAgent;
import game.Board;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BenchmarkSuite {

    public static void runBenchmark(int runsPerDifficulty) {
        System.out.println("Starting Benchmark Suite... running " + runsPerDifficulty + " games per difficulty.");
        
        String csvFile = "benchmark_results.csv";
        
        try (FileWriter out = new FileWriter(csvFile, StandardCharsets.UTF_8);
             CSVPrinter printer = new CSVPrinter(out, CSVFormat.Builder.create(CSVFormat.DEFAULT).setHeader(
                     "Difficulty", "Width", "Height", "Mines", "Result", "Completion_%",
                     "TimeMs", "CSP_Moves", "Probabilistic_Guesses").build())) {
            
            // Beginner: 9x9, 10 mines
            runDifficultySet(printer, "Beginner", 9, 9, 10, runsPerDifficulty);
            
            // Intermediate: 16x16, 40 mines
            runDifficultySet(printer, "Intermediate", 16, 16, 40, runsPerDifficulty);
            
            // Expert: 30x16, 99 mines
            runDifficultySet(printer, "Expert", 30, 16, 99, runsPerDifficulty);
            
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
        
        System.out.println("Benchmarking complete. Results saved to " + csvFile);
    }

    public static void runCustomBenchmark(int runs, int width, int height, int mines) {
        System.out.println("Starting Custom Benchmark... running " + runs + " games of size " + width + "x" + height + " with " + mines + " mines.");
        
        String csvFile = "benchmark_results.csv";
        boolean fileExists = new java.io.File(csvFile).exists();
        
        try (FileWriter out = new FileWriter(csvFile, StandardCharsets.UTF_8, true);
             CSVPrinter printer = new CSVPrinter(out, fileExists ? CSVFormat.DEFAULT : CSVFormat.Builder.create(CSVFormat.DEFAULT).setHeader(
                     "Difficulty", "Width", "Height", "Mines", "Result", "Completion_%",
                     "TimeMs", "CSP_Moves", "Probabilistic_Guesses").build())) {
            
            runDifficultySet(printer, "Custom", width, height, mines, runs);
            
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
        
        System.out.println("Custom Benchmarking complete. Results saved to " + csvFile);
    }

    private static void runDifficultySet(CSVPrinter printer, String difficulty, int width, int height, int mines, int runs) throws IOException {
        int wins = 0;
        
        for (int i = 0; i < runs; i++) {
            Board board = new Board(width, height, mines);
            IntelligentAgent agent = new IntelligentAgent(board);
            
            // Initial click to start the game safely (usually center or 0,0)
            board.reveal(width / 2, height / 2);
            
            long startTime = System.currentTimeMillis();
            agent.solve();
            long endTime = System.currentTimeMillis();
            
            long timeMs = endTime - startTime;
            String result = board.isGameWon() ? "Win" : "Loss";
            if (board.isGameWon()) wins++;
            
            double completionPercent = 100.0;
            if (!board.isGameWon()) {
                int totalSafe = (width * height) - mines;
                int revealedSafe = totalSafe - board.getUnrevealedSafeTiles();
                completionPercent = ((double) revealedSafe / totalSafe) * 100.0;
            }
            
            printer.printRecord(
                    difficulty, width, height, mines, result, String.format("%.2f", completionPercent),
                    timeMs, agent.getCspMovesMade(), agent.getProbabilisticGuessesMade()
            );
        }
        
        System.out.println(difficulty + " completed. Win rate: " + ((double)wins / runs * 100) + "%");
    }
}

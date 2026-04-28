package game;

import agent.IntelligentAgent;
import benchmark.BenchmarkSuite;
import ui.MinesweeperUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting the MSIA...");

        // Change this to "BENCHMARK" to run the automated tests
        // Change this to "UI" to watch the agent visually
        String MODE = "BENCHMARK";

        // Settings for "UI" Mode:
        int uiWidth = 16;
        int uiHeight = 16;
        int uiMines = 40;

        // Settings for "BENCHMARK" Mode:
        int benchmarkRuns = 100;
        boolean runCustomBenchmark = false; // Set to true to run a custom board size below instead of the 3 standard
                                            // difficulties
        int benchmarkWidth = 20;
        int benchmarkHeight = 20;
        int benchmarkMines = 50;

        // Override with command line arguments if provided
        if (args.length > 0) {
            MODE = args[0].toUpperCase();
        }

        if (MODE.equals("BENCHMARK")) {
            if (args.length > 1) {
                try {
                    benchmarkRuns = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number of runs provided. Defaulting to " + benchmarkRuns);
                }
            }

            if (args.length >= 5) {
                try {
                    int width = Integer.parseInt(args[2]);
                    int height = Integer.parseInt(args[3]);
                    int mines = Integer.parseInt(args[4]);
                    BenchmarkSuite.runCustomBenchmark(benchmarkRuns, width, height, mines);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid custom board parameters. Running standard benchmark instead.");
                    BenchmarkSuite.runBenchmark(benchmarkRuns);
                }
            } else if (runCustomBenchmark) {
                BenchmarkSuite.runCustomBenchmark(benchmarkRuns, benchmarkWidth, benchmarkHeight, benchmarkMines);
            } else {
                // Standard benchmark
                BenchmarkSuite.runBenchmark(benchmarkRuns);
            }
        } else {
            // Run the UI for visualization
            if (args.length >= 3) {
                try {
                    uiWidth = Integer.parseInt(args[0]);
                    uiHeight = Integer.parseInt(args[1]);
                    uiMines = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid board parameters. Defaulting to " + uiWidth + "x" + uiHeight + " with "
                            + uiMines + " mines.");
                }
            }

            System.out.println(
                    "Running in UI mode. Board size: " + uiWidth + "x" + uiHeight + " with " + uiMines + " mines.");
            System.out.println("To run benchmarks, change the MODE variable in Main.java to \"BENCHMARK\".");

            Board board = new Board(uiWidth, uiHeight, uiMines);
            IntelligentAgent agent = new IntelligentAgent(board);

            SwingUtilities.invokeLater(() -> {
                MinesweeperUI ui = new MinesweeperUI(board, agent);
                ui.display();
            });
        }
    }
}

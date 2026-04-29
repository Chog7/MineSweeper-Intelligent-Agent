# MineSweeper Intelligent Agent (MSIA)

## Github Link
https://github.com/Chog7/MineSweeper-Intelligent-Agent.git 

## Overview
This project implements a hybrid MineSweeper solver that combines constraint satisfaction, probability inferencing, and visualization. The agent is capable of solving Minesweeper boards of varying difficulties (Easy to Hard).

## Features
- **Constraint Satisfaction**: Uses constraint propagation and backtracking to flag mines and clear safe spaces with 100% certainty.
- **Probabilistic Reasoning**: Calculates the probability of a mine being in each unrevealed tile when no certain moves are available, selecting the tile with the lowest risk.
- **Visualization**: Highlights flagged mines and color-codes hidden tiles by their calculated risk levels to visualize the agent's thought process.
- **Analysis & Benchmarking**: Compares the agent's solve rate and speed against varying board sizes and human equivalence.

## Setup Instructions
1. Download and open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse, or VS Code).
2. The IDE will automatically detect the `pom.xml` and download the necessary dependencies (like `commons-csv`).
3. Ensure you have the appropriate Java Development Kit (JDK 11+) installed.
4. The program supports both UI and benchmark modes. You can switch between them in `src/main/java/game/Main.java`.

## Running the Application

### Visualization (UI Mode)
To see the agent solve the board visually:
- Ensure the program is set to "UI" mode within `src/main/java/game/Main.java`.
- Run `src/main/java/game/Main.java` directly from your IDE.
- Click the "Step Agent" button to watch the AI make logical deductions or probabilistic guesses.

### Benchmarking (Data Collection)
To run the agent hundreds of times and generate a CSV file for your report:
- Pass the argument `BENCHMARK` to `Main.java` when running it to switch to benchmarking mode.
- Optionally, pass a second argument to specify the number of runs per difficulty (e.g., `benchmark 100`).
- The results will be saved to `benchmark_results.csv` in the root project directory.

## Project Structure
- `src/main/java/agent/`: Core AI logic (CSP Solver, Probability Inference).
- `src/main/java/game/`: Minesweeper game mechanics, board state.
- `src/main/java/ui/`: Visualization and user interface.
- `src/main/java/benchmark/`: Headless simulation and CSV data export.

## Contributors
- [Michael Meyers - Chog7]
- [Sarah Stenhouse - sstenh1]
- [Isabel Bacas - ibacas1]

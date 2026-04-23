# MineSweeper Intelligent Agent (MSIA)

## Overview
This project implements a hybrid MineSweeper solver that combines constraint satisfaction, probability inferencing, and visualization. The agent is capable of solving Minesweeper boards of varying difficulties (Easy to Hard).

## Features
- **Constraint Satisfaction**: Uses constraint propagation and backtracking to flag mines and clear safe spaces with 100% certainty.
- **Probabilistic Reasoning**: Calculates the probability of a mine being in each unrevealed tile when no certain moves are available, selecting the tile with the lowest risk.
- **Visualization**: Highlights flagged mines and color-codes hidden tiles by their calculated risk levels to visualize the agent's thought process.
- **Analysis & Benchmarking**: Compares the agent's solve rate and speed against varying board sizes and human equivalence.

## Setup Instructions
1. Clone the repository: `git clone <your-repo-url>`
2. Open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse, or VS Code).
3. Ensure you have the appropriate Java Development Kit (JDK 11+) installed.

## Project Structure
- `src/main/java/agent/`: Core AI logic (CSP Solver, Probability Inference).
- `src/main/java/game/`: Minesweeper game mechanics, board state.
- `src/main/java/ui/`: Visualization and user interface.
- `src/test/java/`: Unit tests and benchmarking scripts.

## Contributors
- Isabel Bacas
- Micheal Meyers
- Sarah Stenhouse 

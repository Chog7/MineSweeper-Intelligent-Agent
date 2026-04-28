package agent;

import game.Board;
import game.Tile;
import java.util.ArrayList;
import java.util.List;

// Handles Probabilistic inference when CSP can't find certain moves
public class ProbabilisticSolver {
    private final Board board;

    public ProbabilisticSolver(Board board) {
        this.board = board;
    }

    // Finds the best guess to make based on naive local probabilities
    // Ties are broken by distance to the center of the board to maximize openness
    // @return the tile with the lowest probability of being a mine, or null if no
    // hidden tiles remain
    public Tile findBestGuess() {
        List<Tile> hiddenTiles = new ArrayList<>();
        int flaggedMines = 0;

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Tile t = board.getTile(x, y);
                if (t.isFlagged()) {
                    flaggedMines++;
                } else if (!t.isRevealed()) {
                    hiddenTiles.add(t);
                }
            }
        }

        if (hiddenTiles.isEmpty())
            return null;

        // Naive Global Probability
        double globalProb = (double) (board.getNumMines() - flaggedMines) / hiddenTiles.size();

        Tile bestTile = hiddenTiles.get(0);
        double lowestProb = 1.1; // Initialize above 1.0
        double closestDist = Double.MAX_VALUE;

        int centerX = board.getWidth() / 2;
        int centerY = board.getHeight() / 2;

        for (Tile hidden : hiddenTiles) {
            double localProb = calculateLocalProbability(hidden);

            // If local probability isn't informative (no revealed neighbors), use global
            double tileProb = (localProb >= 0) ? localProb : globalProb;

            // Calculate distance to center for tie breaking
            double distToCenter = Math.pow(hidden.getX() - centerX, 2) + Math.pow(hidden.getY() - centerY, 2);

            if (tileProb < lowestProb) {
                lowestProb = tileProb;
                bestTile = hidden;
                closestDist = distToCenter;
            } else if (Math.abs(tileProb - lowestProb) < 0.0001) { // Tie breaker
                if (distToCenter < closestDist) {
                    lowestProb = tileProb;
                    bestTile = hidden;
                    closestDist = distToCenter;
                }
            }
        }

        return bestTile;
    }

    // Calculates a naive local probability for a given hidden tile based on its
    // revealed neighbors
    // @return The highest probability from any neighbor, or -1.0 if it has no
    // revealed neighbors
    private double calculateLocalProbability(Tile hiddenTile) {
        List<Tile> neighbors = board.getNeighbors(hiddenTile.getX(), hiddenTile.getY());
        double maxLocalProb = -1.0;

        for (Tile neighbor : neighbors) {
            if (neighbor.isRevealed() && neighbor.getAdjacentMines() > 0) {
                int clue = neighbor.getAdjacentMines();
                List<Tile> neighborOfNeighbor = board.getNeighbors(neighbor.getX(), neighbor.getY());

                int flaggedCount = 0;
                int hiddenCount = 0;

                for (Tile n2 : neighborOfNeighbor) {
                    if (n2.isFlagged())
                        flaggedCount++;
                    else if (!n2.isRevealed())
                        hiddenCount++;
                }

                if (hiddenCount > 0) {
                    double prob = (double) (clue - flaggedCount) / hiddenCount;
                    if (prob > maxLocalProb) {
                        maxLocalProb = prob;
                    }
                }
            }
        }

        return maxLocalProb;
    }
}

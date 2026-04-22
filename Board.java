package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Represents the 2D grid of tiles and where the mines are located.
// Our backend version of the game.
public class Board {
    private final int width;
    private final int height;
    private final int numMines;
    private Tile[][] grid;
    private boolean isGameOver;
    private boolean isGameWon;
    private boolean firstMove;
    private int unrevealedSafeTiles;

    public Board(int width, int height, int numMines) {
        this.width = width;
        this.height = height;
        this.numMines = numMines;
        this.grid = new Tile[width][height];
        this.isGameOver = false;
        this.isGameWon = false;
        this.firstMove = true;
        this.unrevealedSafeTiles = (width * height) - numMines;

        initializeGrid();
    }

    private void initializeGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Tile(x, y);
            }
        }
    }

    private void placeMines(int firstClickX, int firstClickY) {
        Random random = new Random();
        int minesPlaced = 0;
       
        // Only enforce the "first click is a zero" rule if the board is big enough to support it
        boolean requireZeroStart = numMines <= (width * height) - 9;

      
        while (minesPlaced < numMines) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            boolean isFirstClickArea;
            if (requireZeroStart) {
                // Protect the exact clicked tile and all 8 of its neighbors
                isFirstClickArea = Math.abs(x - firstClickX) <= 1 && Math.abs(y - firstClickY) <= 1;
            } else {
                // Fallback for extremely dense boards: only protect the exact clicked tile
                isFirstClickArea = (x == firstClickX && y == firstClickY);
            }

            // Ensure we don't place a mine on the first clicked area or if there's already a mine
            if (!grid[x][y].isMine() && !isFirstClickArea) {
                grid[x][y].setMine(true);
                minesPlaced++;
            }
        }

        calculateAdjacentMines();
    }

    private void calculateAdjacentMines() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!grid[x][y].isMine()) {
                    int count = 0;
                    for (Tile neighbor : getNeighbors(x, y)) {
                        if (neighbor.isMine()) {
                            count++;
                        }
                    }
                    grid[x][y].setAdjacentMines(count);
                }
            }
        }
    }

    public List<Tile> getNeighbors(int x, int y) {
        List<Tile> neighbors = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx;
                int ny = y + dy;
                if (isValidCoordinate(nx, ny)) {
                    neighbors.add(grid[nx][ny]);
                }
            }
        }
        return neighbors;
    }

    public boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void reveal(int x, int y) {
        if (isGameOver || isGameWon) {
            System.out.println("Game has ended.");
            return;
        }
        
        if (!isValidCoordinate(x, y)) {
            System.out.println("Invalid move.");
            return;
        }

        Tile tile = grid[x][y];
        
        if (tile.isRevealed()) {
           System.out.println("Tile is already revealed.");
            return;
        }

        if (tile.isFlagged()) {
           System.out.println("Tile is flagged.");
            return;
        }
        
        if (firstMove) {
            placeMines(x, y);
            firstMove = false;
        }

        tile.setRevealed(true);

        if (tile.isMine()) {
            isGameOver = true;
            return;
        }

        unrevealedSafeTiles--;
        if (unrevealedSafeTiles == 0) {
            isGameWon = true;
            return;
        }

        // Auto-reveal neighbors if 0 adjacent mines
        if (tile.getAdjacentMines() == 0) {
            for (Tile neighbor : getNeighbors(x, y)) {
                if (!neighbor.isRevealed() && !neighbor.isFlagged()) {
                    reveal(neighbor.getX(), neighbor.getY());
                }
            }
        }
    }

    public void flag(int x, int y) {
        if (isGameOver || isGameWon || !isValidCoordinate(x, y)) return;

        Tile tile = grid[x][y];
        if (!tile.isRevealed()) {
            tile.setFlagged(!tile.isFlagged());
        }
    }

    // Getters
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getNumMines() { return numMines; }
    public Tile getTile(int x, int y) { return grid[x][y]; }
    public boolean isGameOver() { return isGameOver; }
    public boolean isGameWon() { return isGameWon; }
    public int getUnrevealedSafeTiles() { return unrevealedSafeTiles; }

    public void printConsoleBoard() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(grid[x][y].toString() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

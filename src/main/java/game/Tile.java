package game;

// Single square on the Minesweeper board.
public class Tile {
    private final int x;
    private final int y;
    private boolean isMine;
    private boolean isRevealed;
    private boolean isFlagged;
    private int adjacentMines;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.isMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.adjacentMines = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }

    @Override
    public String toString() {
        if (isFlagged)
            return "F";
        if (!isRevealed)
            return "?";
        if (isMine)
            return "*";
        if (adjacentMines == 0)
            return " ";
        return String.valueOf(adjacentMines);
    }
}

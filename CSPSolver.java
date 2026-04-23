package agent;

import game.Board;
import game.Tile;
import java.util.ArrayList;
import java.util.List;

//handles constraint satisfaction problem logic for the agent 
public class CSPSolver { 
private final Board board; 

public CSPSolver (Board board){
  this.board = board; 
}
  
//iterates through the board and applies simple constraint logic 
//@return true if any action (reveal or flag) was taken, false otherwise 
public boolean solveSingleStep() {
  boolean actionTaken = false; 

  for (int x = 0; x < board.getWidth(); x++) {
    for (int y = 0; y < board.getHeight(); y++) { 
    Tile tile = board.getTile(x, y); 

    //we only care about revealed clues greater than 0 
    if (tile.isRevealed() && tile.getAdjacentMines() > 0) {
      List<Tile> neighbors = board.getNeighbors(x, y); 
      int hiddenCount = 0; 
      int flaggedCount = 0; 
      List<Tile> hiddenNeighbors = new ArrayList<>();

      for (Tile neighbor : neighbors) {
        if (neighbor.isFlagged()) {
          flaggedCount++;
          } else if (!neighbor.isRevealed()) {
          hiddenCount++;
          hiddenNeighbors.add(neighbor);
        }
      }


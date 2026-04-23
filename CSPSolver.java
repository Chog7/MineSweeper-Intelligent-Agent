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

      //Rule 1: all hidden are Mines 
      //if the number of hidden neighbors equals the remaning mines for this clue, 
      //then all the hidden neighbors MUST be mines. 
      if (hiddenCount > 0 && tile.getAdjacentMines() - flaggedCount == hiddenCount) {
        for (Tile hidden : hiddenNeighbors) {
          if (!hidden.isFlagged()) {
            board.flag(hidden.getX(), hidden.getY());
            actionTaken = true;
          }
        }
      }

      //Rule 2: All hidden are Safe 
      //if the number of flagged neighbors equals the clue number, 
      //then all remaining hidden neighbors MUST be safe 
      if (hiddenCount > 0 && flaggedCount == tile.getAdjacentMines()) {
        for (Tile hidden : hiddenNeighbors) {
          if (!hidden.isRevealed() && !hidden.isFlagged()) {
            board.reveal(hidden.getX(), hidden.getY());
            actionTaken = true;
          }
        }
      }
    }
    }
  } 
  
  return actionTaken;
}
}

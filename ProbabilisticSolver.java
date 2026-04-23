package agent; 
import game.Board;
import game.Tile;
import java.util.List; 

//Handles Probabilistic inference when CSP cannot find certain moves 
public class ProbabilisticSolver { 
  private final Board board; 

public ProbabilisticSolver(Board board) {
  this.board = board; 
} 

//Finds the best guess to make based on naive local probabilities 
//Ties are broken by distance to the center of the board to maximize openness 
//@return the tile with the lowest probability of being a mine, or null if no 
//hidden tiles remain
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

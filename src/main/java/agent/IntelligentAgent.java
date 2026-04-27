package agent; 

import game.Board; 
import game.Tile; 

//The core brain that controls the decision making 
//Combines CSP and Probabilistic reasoning 
public class IntelligentAgent {
    private final Board board;
    private final CSPSolver cspSolver;
    private final ProbabilisticSolver probSolver;

    // Tracking metrics for benchmarking
    private int cspMovesMade = 0;
    private int probabilisticGuessesMade = 0;

    public IntelligentAgent(Board board) {
        this.board = board;
        this.cspSolver = new CSPSolver(board);
        this.probSolver = new ProbabilisticSolver(board);
    }

  //Executes one step of reasoning 
  //@return A string describing the action taken 
  public String step() { 
    if (board.isGameOver() || board.isGameWon()) {
      return "Game is over."; 
    }
      
    // 1. Try CSP logic (100% certain moves)
    boolean cspMadeMove = cspSolver.solveSingleStep();
      if (cspMadeMove) {
          cspMovesMade++;
          return "CSP logic executed certain moves.";
      }
    
    // 2. If no certain moves, fallback to Probabilistic logic
    Tile bestGuess = probSolver.findBestGuess();
      if (bestGuess != null) {
          board.reveal(bestGuess.getX(), bestGuess.getY());
          probabilisticGuessesMade++;
          return "Probabilistic logic guessed tile (" + bestGuess.getX() + ", " + bestGuess.getY() + ").";
      }
      
      return "No moves available.";
  }
    
    // Solves the board completely using a loop
    // Useful for benchmarking without UI
    public void solve() {
        while (!board.isGameOver() && !board.isGameWon()) {
            String result = step();
            if (result.equals("No moves available.")) {
                break;
            }
        }
    }

    // Getters for metrics
    public int getCspMovesMade() {
        return cspMovesMade;
    }
    
    public int getProbabilisticGuessesMade() {
        return probabilisticGuessesMade;
    }
}


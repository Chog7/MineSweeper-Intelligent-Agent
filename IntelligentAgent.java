package agent; 

import game.Board; 
import game.Tile; 

//The core brain that controls the decision making 
//Combines CSP and Probalistic reasoning 
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
    if (board.isGameOver() || board.isGameWon() {
      return "Game is over."; 
    }

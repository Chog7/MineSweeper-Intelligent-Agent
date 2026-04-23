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
  

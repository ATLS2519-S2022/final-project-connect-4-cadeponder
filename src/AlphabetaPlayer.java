
public class AlphabetaPlayer implements Player {
//	function alphabeta(node, depth, alpha, beta, maximizingPlayer) { 
//	if depth ==0 or node is a terminal node then  
//	return static evaluation of node  
//	  
//	if MaximizingPlayer then      // for Maximizer Player  
//	   maxEva= -infinity            
//	   for each child of node do  
//	   eva= alphabeta(child, depth-1, alpha, beta, False)  
//	  maxEva= max(maxEva, eva)   
//	  alpha= max(alpha, maxEva)      
//	   if beta<=alpha  
//	 break  
//	 return maxEva  
//	    
//	else                         // for Minimizer player  
//	   minEva= +infinity   
//	   for each child of node do  
//	   eva= alphabeta(child, depth-1, alpha, beta, true)  
//	   minEva= min(minEva, eva)   
//	   beta= min(beta, eva)  
//	    if beta<=alpha  
//	  break          
//	 return minEva 
//	}
	int id; 
    int cols; 
    
    @Override
    public String name() {
        return "Alpha Beta Player";
    }
	  @Override
	    public void init(int id, int msecPerMove, int rows, int cols) {
	    	this.id = id; //id is your player's id, opponent's id is 3-id
	    	this.cols = cols;
	    	}
	   @Override
	    public void calcMove(Connect4Board board, int oppMoveCol, Arbitrator arb) 
	        throws TimeUpException {
	        // Make sure there is room to make a move.
	        if (board.isFull()) {
	            throw new Error ("Complaint: The board is full!");
	        }
	        
	        int move = 0; //index of best row
	        int maxDepth = 1;
	        int maxScore = -1000;
	        int[] bestScoresArray= new int[7];
	        
	        // while there's time left and maxDepth <= number of moves remaining
	        while(!arb.isTimeUp() && maxDepth <= board.numEmptyCells()) {
	        	// do alphabeta search
	        	// start the first level of alphabeta, set move as you're finding the bestScore
	        	for (int depth=0; depth<maxDepth; depth++) { //iterates through to maxDepth
	        		for (int i=0; i<cols; i++) { //iterates through 7 cols
	        			if (board.isValidMove(i)) {//if valid move
	        				board.move(i, id);
	        				bestScoresArray[i]= alphabeta(board, depth,-1000,1000, true,arb); 
	        				board.unmove(i, id);
	        					if (bestScoresArray[i]>maxScore) {
	        						maxScore=bestScoresArray[i];
	                				move = i; 
	        				}
	        			}
	        		 }
	            	arb.setMove(move); 
	        	}
	            maxDepth++;
	        }        
	    }
	   
	 public int alphabeta(Connect4Board board, int depth,int alpha,int beta,boolean isMaximizing, Arbitrator arb) {
		 
		 if (depth == 0 || board.numEmptyCells() == 0 || arb.isTimeUp()) {
	    		return score(board);
	    	}
		 
		 if (isMaximizing) {
			 int bestScore = -1000;
			 for (int i=0; i<cols; i++) {
				 if (board.isValidMove(i)) {
						board.move(i, id);
	    				bestScore = Math.max(bestScore, alphabeta(board, depth - 1,alpha,beta, false, arb)); 
	    				alpha=Math.max(alpha, bestScore);
	    				if (alpha>=beta) {
	    					break;
	    				}
	    				board.unmove(i, id);
	    		}
			 }
			 return bestScore;
		 }
		 else {
			 int bestScore = 1000;
			 for (int i=0; i<cols; i++) {
				 if (board.isValidMove(i)) {
						board.move(i, id);
	    				bestScore = Math.min(bestScore, alphabeta(board, depth - 1,alpha,beta, true, arb)); 
	    				beta=Math.min(beta, bestScore);
	    				if (alpha>=beta) {
	    					break;
	    				}
	    				board.unmove(i, id);
	    		}
			 } 
			 return bestScore;
		 }
	 }
	   
	    public int alphabeta(Connect4Board board, int depth, boolean isMaximizing, Arbitrator arb) {
	    	
//	    	if depth = 0 or there's no moves left or time is up
//	    			return the heuristic value of node 
	    	
	    	if (depth == 0 || board.numEmptyCells() == 0 || arb.isTimeUp()) {
	    		return score(board);
	    	}
	    	
	    	if (isMaximizing) {
	    		int bestScore = -1000;
	    		for(int i=0; i<cols; i++) {
	    			if (board.isValidMove(i)) {
						board.move(i, id);
	    				bestScore = Math.max(bestScore, alphabeta(board, depth - 1, false, arb)); 
	    				board.unmove(i, id);
	    			}
	    		}
	    		return bestScore;
	    	}
	    	
	    	
	    	else {
	    		int bestScore = 1000;
	    		for(int i=0; i<cols; i++) {
	    			if (board.isValidMove(i)) {
	    				board.move(i, 3-id);
	    				bestScore = Math.min(bestScore, alphabeta(board, depth - 1, true, arb)); 
	    				board.unmove(i, 3-id);
	    			}
	    		}
	    		return bestScore;
	    	}
	    }
	    
	 public int score(Connect4Board board) {
	    	return calcScore(board, id) - calcScore(board, 3-id);
	    }
	 
	public int calcScore(Connect4Board board, int id)
	{
		final int rows = board.numRows();
		final int cols = board.numCols();
		int score = 0;
		// Look for horizontal connect-4s.
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c <= cols - 4; c++) {
				if (board.get(r, c + 0) != id) continue;
				if (board.get(r, c + 1) != id) continue;
				if (board.get(r, c + 2) != id) continue;
				if (board.get(r, c + 3) != id) continue;
				score++;
			}
		}
		// Look for vertical connect-4s.
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c) != id) continue;
				if (board.get(r + 1, c) != id) continue;
				if (board.get(r + 2, c) != id) continue;
				if (board.get(r + 3, c) != id) continue;
				score++;
			}
		}
		// Look for diagonal connect-4s.
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c + 0) != id) continue;
				if (board.get(r + 1, c + 1) != id) continue;
				if (board.get(r + 2, c + 2) != id) continue;
				if (board.get(r + 3, c + 3) != id) continue;
				score++;
			}
		}
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = rows - 1; r >= 4 - 1; r--) {
				if (board.get(r - 0, c + 0) != id) continue;
				if (board.get(r - 1, c + 1) != id) continue;
				if (board.get(r - 2, c + 2) != id) continue;
				if (board.get(r - 3, c + 3) != id) continue;
				score++;
			}
		}
		return score;
	}
}

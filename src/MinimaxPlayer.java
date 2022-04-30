//pseudo from Ruojia
public class MinimaxPlayer implements Player {
	int id; 
    int cols; 
    
    @Override
    public String name() {
        return "Minimax Player";
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
        	// do minimax search
        	// start the first level of minimax, set move as you're finding the bestScore
        	for (int depth=0; depth<maxDepth; depth++) { //iterates through to maxDepth
        		for (int i=0; i<cols; i++) { //iterates through 7 cols
        			if (board.isValidMove(i)) {//if valid move
        				board.move(i, id);
        				bestScoresArray[i]= minimax(board, depth, true,arb); 
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

    public int minimax(Connect4Board board, int depth, boolean isMaximizing, Arbitrator arb) {
    	
//    	if depth = 0 or there's no moves left or time is up
//    			return the heuristic value of node 
    	
    	if (depth == 0 || board.numEmptyCells() == 0 || arb.isTimeUp()) {
    		return score(board);
    	}
    	
    	if (isMaximizing) {
    		int bestScore = -1000;
    		for(int i=0; i<cols; i++) {
    			if (board.isValidMove(i)) {
					board.move(i, id);
    				bestScore = Math.max(bestScore, minimax(board, depth - 1, false, arb)); 
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
    				bestScore = Math.min(bestScore, minimax(board, depth - 1, true, arb)); 
    				board.unmove(i, 3-id);
    			}
    		}
    		return bestScore;
    	}
    }
    
    // your score - opponent's score
    public int score(Connect4Board board) {
    	return calcScore(board, id) - calcScore(board, 3-id);
    }
    
	// Return the number of connect-4s that player #id has.
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

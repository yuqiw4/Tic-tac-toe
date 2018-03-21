package tictactoe;


public class Tictactoe {
	
    //variables
	//String x,o;
	static final char empty = '-';
	static final int INFINITY = 100;
	static final int WIN = + INFINITY;
	static final int LOSE = - INFINITY;
	static final int DOUBLE_LINK = 30;
	static final int INPROGRESS = 1;
	static final int DRAW = 0;
	
	static char[] gameboard = {
			'-', '-', '-', 
			'-', '-', '-', 
			'-', '-', '-'
	};
	
	static final int [] INITIAL_POS_VALUE = {
			3, 2, 3, 
			2, 4, 2, 
			3, 2, 3
	};
	
	static final int [][] WIN_STATUS = {
			{ 0, 1, 2 }, 
			{ 3, 4, 5 }, 
			{ 6, 7, 8 }, 
			{ 0, 3, 6 }, 
			{ 1, 4, 7 }, 
			{ 2, 5, 8 },
			{ 0, 4, 8 }, 
			{ 2, 4, 6 }
	};
	
	
	//constructor
	public Tictactoe() {
		
	}
	
	//public method
	
	public static int gameState (char[] board){
		int result = INPROGRESS;
		boolean isFull = true;
		int sum = 0;
		int index = 0;
		
		int num_x = 0;
		int num_o = 0;
		char whosNext;
		
		for(char k : board){
			if(k == 'x'){
				num_x++;
			}else if(k == 'o'){
				num_o++;
			}
		}
		
		if(num_x == num_o){
			whosNext = 'x';
		}else{
			whosNext = 'o';
		}
		
		
		//check if game is over
		for ( int pos = 0; pos < 9; pos++ ){
			char chess = board[pos];
			if( empty == chess ){
				isFull = false;
			}
			else{
				sum += chess;
				index = pos;
			}
		}
		
		//is initial?
		boolean isInitial = (sum == 'x' || sum == 'o');
		if (isInitial){
			return (sum == 'x' ?1:-1)*INITIAL_POS_VALUE[index];
		}
		
		//is 'x' win/lose?
		for ( int[] status : WIN_STATUS ){
			char chess = board[status[0]];
			if(chess == empty){
				continue;
			}
			int i = 1;
			for(; i < status.length; i++){
				if(board[status[i]] != chess){
					break;
				}
			}
			if(i == status.length){
				result = chess == 'x' ? WIN:LOSE;
				break;
			}
		}
				
		
			if(result != WIN && result != LOSE){
				if(isFull){
					result = DRAW;
				}
				else{
					
					//check double link
					int [] finds = new int [2];
					for( int [] status : WIN_STATUS){
						char chess = empty;
						boolean hasEmpty = false;
						int count = 0;
						for(int i = 0; i < status.length; i++){
							if(board[status[i]] == empty){
								hasEmpty = true;
							}else{
								if (chess == empty){
									chess = board[status[i]];
								}
								if( board[status[i]] == chess){
									count++;
								}
							}
						}
						
						if(hasEmpty && count > 1){
							if(chess == 'x'){
								finds[0]++;
							}else{
								finds[1]++;
							}
						}
					}
					
					if(finds[1]>0 && whosNext == 'o'){
						result = -DOUBLE_LINK;
					}else if(finds[0]>0 && whosNext == 'x'){
						result = DOUBLE_LINK;
					}else if(finds[1]>0 && whosNext == 'o'){
						result = -DOUBLE_LINK / 2;
					}else if(finds[0]>0 && whosNext == 'o'){
						result = DOUBLE_LINK / 2;
					}
				}
			}
		
		
		return result;
	}
	
	
	
	
	//minimax for 'x'
	
	public static int minimaxX( char[] board, int depth){
		
		int [] bestMoves = new int[9];
		int index = 0;
		
		int bestValue = -INFINITY;
		for( int pos = 0; pos < 9; pos++){
			
			//System.out.println("INSIDE MINIMAX X");
			//System.out.println(board[pos] == empty);
			
			if(board[pos] == empty){
				//bestMoves[index] = pos;
				board[pos] = 'x';
				
				int value = max(board, depth);

				if(value>bestValue){
					
					bestValue = value;
					index = 0;
					bestMoves[index] = pos;
					
				}
				/*else if(value == bestValue){
					index++;
					bestMoves[index] = pos;
				}*/
				
				board[pos] = empty;
				
			}
		}
		
		return bestMoves[index];
	}
	
	//minimax for 'o'
	public static int minimaxO( char[] board, int depth){
		int [] worstMoves = new int[9];
		int index = 0;
		
		int bestValue = + INFINITY;
		for(int pos = 0; pos < 9; pos++){
			
			//System.out.println("INSIDE MINIMAX O");
			//System.out.println(board[pos] == empty);
			
			if(board[pos] == empty){
				//worstMoves[index] = pos;
				board[pos] = 'o';
				
				int value = min(board,depth);
				if(value<bestValue){
					
					bestValue = value;
					index = 0;
					worstMoves[index] = pos;
					
				}else if(value == bestValue){
					
					index++;
					worstMoves[index] = pos;
					
				}
				
				board[pos] = empty;
				
			}
		}
		
		return worstMoves[index];
	}

	
	public static int max(char [] board, int depth){
		
		int evalValue = gameState (board);
		
		boolean isGameOver = (evalValue == WIN || evalValue == LOSE || evalValue == DRAW);
		
		if(depth == 0 || isGameOver){
			return evalValue;
		}
		
		int bestValue = -INFINITY;
		for(int pos = 0; pos < 9; pos++){
			
			if(board[pos] == empty){
				board[pos]  = 'x';
				bestValue = Math.max(bestValue, min(board, depth-1));
				board[pos] = empty;
			}
		}
		
		
		return evalValue;
	}
	
	public static int min(char [] board, int depth){
		
		int evalValue = gameState(board);
		
		boolean isGameOver = (evalValue == WIN || evalValue == LOSE || evalValue == DRAW);
		
		if(depth == 0 || isGameOver){
			return evalValue;
		}
		
		int bestValue = + INFINITY;
		for(int pos = 0; pos < 9; pos++){
			if(board[pos] ==empty){
				board[pos] = 'o';
				bestValue = Math.min(bestValue, max(board, depth-1));
				board[pos] = empty;
			}
		}
		
		
		return evalValue;
	}
	

	
	public void run(int depth){
		int round = 1;
		
		while(gameState(Tictactoe.gameboard) != WIN && gameState(Tictactoe.gameboard) != LOSE && gameState(Tictactoe.gameboard) != DRAW){
			if(round%2 == 1){

				int bestX = minimaxX(Tictactoe.gameboard, depth);

				Tictactoe.gameboard[bestX] = 'x';
				
			}else{
				
				int bestO = minimaxO(Tictactoe.gameboard, depth);
				Tictactoe.gameboard[bestO] = 'o';
				
			}
			System.out.println(Tictactoe.gameboard);
			round++;

		}
		
		if(gameState(Tictactoe.gameboard) == DRAW){
			System.out.println("DRAW");
		}else if(gameState(Tictactoe.gameboard) == WIN){
			System.out.println("WIN");
		}else if(gameState(Tictactoe.gameboard) == LOSE){
			System.out.println("LOSE");
		}
		
	}
	
	public static void main(String [] args){
		Tictactoe game = new Tictactoe();
		game.run(1);
	}
	
}

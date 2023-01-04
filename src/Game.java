public class Game {
	public static final int PLAYER_NONE = 0;
	public static final int PLAYER_ONE = 1;
	public static final int PLAYER_TWO = 2;

	private int[][] board;

	public Game() {
		this.board = new int[3][3];
	}

	public int getPlacement(int row, int col) {
		return board[row][col];
	}

	public void setPlacement(int row, int col, int player) {
		board[row][col] = player;
	}

	private int checkLine(int startRow, int startCol, int offsetRow, int offsetCol) {
		int startPlacement = getPlacement(startRow, startCol);
		for (
				int row = startRow, col = startCol;
				0 <= row && row < 3 &&
					0 <= col && col < 3;
				row += offsetRow, col += offsetCol) // lol
		{
			if (getPlacement(row, col) != startPlacement) {
				return PLAYER_NONE;
			}
		}
		return startPlacement;
	}

	public int winner() {
		int row0 = checkLine(0, 0, 0, 1);
		int row1 = checkLine(1, 0, 0, 1);
		int row2 = checkLine(2, 0, 0, 1);
		
		int col0 = checkLine(0, 0, 1, 0);
		int col1 = checkLine(0, 1, 1, 0);
		int col2 = checkLine(0, 2, 1, 0);

		int dia0 = checkLine(0, 0, 1, 1);
		int dia1 = checkLine(0, 2, 1, -1);

		if (row0 != PLAYER_NONE)
			return row0;
		if (row1 != PLAYER_NONE)
			return row1;
		if (row2 != PLAYER_NONE)
			return row2;
		
		if (col0 != PLAYER_NONE)
			return col0;
		if (col1 != PLAYER_NONE)
			return col1;
		if (col2 != PLAYER_NONE)
			return col2;

		if (dia0 != PLAYER_NONE)
			return dia0;
		if (dia1 != PLAYER_NONE)
			return dia1;

		return PLAYER_NONE;
	}

	public boolean isPlayer1Winner() {
		return winner() == PLAYER_ONE;
	}

	public boolean isPlayer2Winner() {
		return winner() == PLAYER_TWO;
	}

	public boolean isTieGame() {
		return winner() == PLAYER_NONE;
	}

	private boolean isBoardFilled() {
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				int placement = getPlacement(row, col);
				if (placement != PLAYER_ONE && placement != PLAYER_TWO) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isGameOver() {
		return isBoardFilled() || isPlayer1Winner() || isPlayer2Winner();
	}

	// Mini testing thing
	public static void main(String[] args) {
		Game game = new Game();

		game.setPlacement(0, 0, PLAYER_ONE);
		game.setPlacement(1, 1, PLAYER_TWO);
		game.setPlacement(2, 2, PLAYER_ONE);
		game.setPlacement(0, 2, PLAYER_ONE);
		game.setPlacement(0, 1, PLAYER_TWO);
		game.setPlacement(1, 2, PLAYER_ONE);

		System.out.println(game.isGameOver());
		System.out.println(game.isPlayer1Winner());
		System.out.println(game.isPlayer2Winner());
		System.out.println(game.isTieGame());
	}
}

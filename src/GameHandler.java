public class GameHandler {
	private Game game;
	private Player player1;
	private Player player2;
	private Player turn;

	public GameHandler(Player player1, Player player2) {
		this.game = new Game();
		this.player1 = player1;
		this.player2 = player2;
		this.turn = null;
	}

	public Game getState() { return game; }
	public Player getTurn() { return turn; }

	public void setPlayer1(Player newPlayer1) { player1 = newPlayer1; }
	public void setPlayer2(Player newPlayer2) { player2 = newPlayer2; }

	private int getPlayerCode() {
		if (turn == player1) {
			return Game.PLAYER_ONE;
		} else {
			return Game.PLAYER_TWO;
		}
	}

	private void switchTurns() {
		if (turn == player1) {
			turn = player2;
		} else {
			turn = player1;
		}
	}

	public void move(int row, int col) {
		int playerCode = getPlayerCode();
		game.setPlacement(row, col, playerCode);
		switchTurns();

		String response = Player.OP_RESPONSE_MOVE
			+ Player.PAYLOAD_DELIMITER + row
			+ Player.PAYLOAD_DELIMITER + col
			+ playerCode;

		player1.send(response);
		player2.send(response);
	}

	public boolean check(int row, int col) {
		return game.getPlacement(row, col) == Game.PLAYER_NONE;
	}

	public void startGame() {
		player1.send(Player.OP_RESPONSE_GAME);
		player2.send(Player.OP_RESPONSE_GAME);
		turn = player2;
		turn.send(Player.OP_REQUEST_MOVE);
	}

	public void endGame() {
		String response = Player.OP_RESPONSE_GAME_ENDED + Player.PAYLOAD_DELIMITER;
		if (game.isPlayer1Winner())
			response += "P1";
		else if (game.isPlayer2Winner())
			response += "P2";
		else
			response += "Tie";

		player1.send(response);
		player2.send(response);

		player1.end();
		player2.end();
	}
}

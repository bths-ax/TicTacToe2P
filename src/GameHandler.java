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
	public void setTurn(Player newTurn) {
		turn = newTurn;
		turn.send(Player.OP_REQUEST_MOVE);
	}

	private int getPlayerCode() {
		if (turn == player1) {
			return Game.PLAYER_ONE;
		} else {
			return Game.PLAYER_TWO;
		}
	}

	private void switchTurns() {
		if (turn == player1) {
			setTurn(player2);
		} else {
			setTurn(player1);
		}
	}

	public void move(int row, int col) {
		int playerCode = getPlayerCode();
		game.setPlacement(row, col, playerCode);
		switchTurns();
	}

	public boolean check(int row, int col) {
		return game.getPlacement(row, col) == Game.PLAYER_NONE;
	}

	public void startGame() {
		player1.send(Player.OP_RESPONSE_GAME);
		player2.send(Player.OP_RESPONSE_GAME);
		setTurn(player2);
	}

	public void endGame() {
		String response = OP_RESPONSE_GAME_ENDED + PAYLOAD_DELIMITER;
		if (game.getState().isPlayer1Winner())
			response += "P1";
		else if (game.getState().isPlayer2Winner())
			response += "P2";
		else
			response += "Tie";

		player1.send(response);
		player2.send(response);

		player1.clearGame();
		player2.clearGame();
	}
}

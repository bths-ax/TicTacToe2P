public class GameHandler {
	private Game game;
	private Player player1;
	private Player player2;

	public GameHandler(Player player1, Player player2) {
		this.game = new Game();
		this.setPlayer1(player1);
		this.setPlayer2(player2);
	}

	public void setPlayer1(Player newPlayer1) { player1 = newPlayer1; }
	public void setPlayer2(Player newPlayer2) { player2 = newPlayer2; }

	public void startGame() {
		player1.send(Player.OP_RESPONSE_GAME);
		player2.send(Player.OP_RESPONSE_GAME);
		player2.send(Player.OP_REQUEST_MOVE);
	}
}

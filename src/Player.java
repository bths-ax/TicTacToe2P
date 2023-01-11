import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class Player implements Runnable {
	public static final String PAYLOAD_DELIMITER = ";";

	// Request:  Queue for game
	// Response: Queued
	public static final String OP_REQUEST_GAME        = "0";
	// Response: Game started
	public static final String OP_RESPONSE_GAME       = "1";
	// Response: Waiting for move
	// Request:  Sent move
	public static final String OP_REQUEST_MOVE        = "2";
	// Response: Opponent move
	public static final String OP_RESPONSE_MOVE       = "3";
	// Response: Game ended
	public static final String OP_RESPONSE_GAME_ENDED = "4";

	private Server server;
	private GameHandler game;

	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public Player(Server server, Socket socket) throws IOException {
		this.server = server;
		this.game = null;
		this.socket = socket;
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.out = new PrintWriter(socket.getOutputStream(), true);
	}

	public void send(String data) {
		out.println(data);
	}

	public void run() {
		try {
			String payloadStr;
			while ((payloadStr = in.readLine()) != null) {
				String[] payload = payloadStr.split(PAYLOAD_DELIMITER);
				String opcode = payload[0];

				if (opcode.equals(OP_REQUEST_GAME)) {
					// Check if player is in a game
					if (game != null) {
						continue;
					}

					// Create new game if none in queue
					if (server.getGameQueue() == null) {
						GameHandler newGame = new GameHandler(this, null);
						server.setGameQueue(newGame);
						game = newGame;
						continue;
					}

					// Enter queued game if one exists
					game = server.getGameQueue();
					game.setPlayer2(this);
					game.startGame();
					server.addGame(game);
				}

				else if (opcode.equals(OP_REQUEST_MOVE)) {
					// Check if player is in a game
					if (game == null) {
						continue;
					}

					// Check if its the players turn
					if (game.getTurn() != this) {
						send(OP_REQUEST_MOVE + PAYLOAD_DELIMITER + "NotUserTurn");
						continue;
					}

					// Check if the placement row and column were sent as payload data
					int placementRow, placementCol;
					try {
						placementRow = Integer.parseInt(payload[1]);
						placementCol = Integer.parseInt(payload[2]);
					} catch (Exception _e) {
						send(OP_REQUEST_MOVE + PAYLOAD_DELIMITER + "InvalidPayloadData");
						continue;
					}

					// Check if the placement row and column are valid indexes
					if (!(0 <= placementRow && placementRow < 3 && 0 <= placementCol && placementCol < 3)) {
						send(OP_REQUEST_MOVE + PAYLOAD_DELIMITER + "InvalidIndices");
						continue;
					}

					// Check if the placement spot is empty
					if (!game.check(placementRow, placementCol)) {
						send(OP_REQUEST_MOVE + PAYLOAD_DELIMITER + "CannotPlace");
						continue;
					}

					// Successfully placed move
					game.move(placementRow, placementCol);

					// Check for winners
					if (game.getState().isGameOver()) {
						game.endGame();
						break;
					}

					// Request move from next player
					game.getTurn().send(OP_REQUEST_MOVE);
				}
			}
		} catch (IOException _e) {}

		end(); // just for good measure ig
	}

	public void end() {
		try {
			socket.close();
		} catch (IOException _e) {}
	}
}

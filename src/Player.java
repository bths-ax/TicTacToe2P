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
					if (server.getGameQueue() == null) {
						GameHandler newGame = new GameHandler(this, null);
						server.setGameQueue(newGame);
						game = newGame;
						continue;
					}

					game = server.getGameQueue();
					game.setPlayer2(this);
					game.startGame();
					server.addGame(game);
				}
			}
		} catch (IOException _e) {}
	}
}

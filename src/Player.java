import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class Player implements Runnable {
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

	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private GameHandler game;

	public Player(Socket socket) throws IOException {
		this.socket = socket;
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.out = new PrintWriter(socket.getOutputStream(), true);
		this.game = null;
	}

	public void run() {
		// TODO: handle like literally everything lmao
	}
}

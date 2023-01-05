import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class Player {
	// Client -> Server: Queue for game
	// Server -> Client: Queued
	public static final String OP_PLAYER_READY = "A";
	// Server -> Client: Entered game
	public static final String OP_GAME_READY = "B";
	// Client -> Server: Place move
	// Server -> Client: Place opponent move
	public static final String OP_PLACE = "C";
	// Server -> Client: Game ended
	public static final String OP_GAME_OVER = "D";

	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public Player(Socket socket) {
		this.socket = socket;
		this.in = new BufferedReader(socket.getInputStream());
		this.out = new PrintWriter(socket.getOutputStream());
	}

	// TODO: like everything lmao
}

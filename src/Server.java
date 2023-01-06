import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
	private ServerSocket server;
	private ArrayList<GameHandler> games;
	private GameHandler gameQueue;

	public Server(int port) throws IOException {
		this.server = new ServerSocket(port);
		this.games = new ArrayList<GameHandler>();
		this.gameQueue = null;
	}

	public void run() throws IOException {
		while (true) {
			Player newPlayer = new Player(server.accept());

			if (gameQueue == null) {
				gameQueue = new GameHandler(newPlayer, null);
				continue;
			}

			gameQueue.setPlayer2(newPlayer);
			games.add(gameQueue);
			gameQueue = null;
		}
	}


	public static void main(String[] args) {
		/* TESTING */ args = new String[] { "6969" };

		if (args.length < 1) {
			System.out.println("Usage: java Server <port>");
			return;
		}

		System.out.println("Starting server on port " + args[0]);

		try {
			Server server = new Server(Integer.parseInt(args[0]));
			server.run();
		} catch (Exception _e) {}
	}
}

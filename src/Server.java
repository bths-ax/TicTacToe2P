import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
	private ServerSocket server;
	private ArrayList<GameHandler> games;
	private GameHandler gameQueue;

	public Server() {
		this.server = new ServerSocket(6969);
		this.games = new ArrayList<GameHandler>();
		this.queued = null;
	}

	public void run() {
		while (true) {
			Player newPlayer = new Player(server.accept());

			if (gameQueue == null) {
				gameQueue = new GameHandler(newPlayer, null);
				continue;
			}

			gameQueue.setPlayer2(newPlayer);
			gameQueue.run();
			games.add(gameQueue);
			gameQueue = null;
		}
	}


	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}
}

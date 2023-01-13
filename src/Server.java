import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
	private ServerSocket server;
	private ArrayList<GameHandler> games; // TODO: what did i make this for is this really needed
	private GameHandler gameQueue;

	public Server(int port) throws IOException {
		this.server = new ServerSocket(port);
		this.games = new ArrayList<GameHandler>();
		this.gameQueue = null;
	}

	public GameHandler getGameQueue() { return gameQueue; }
	public void setGameQueue(GameHandler newGameQueue) { gameQueue = newGameQueue; }

	public void addGame(GameHandler game) { games.add(game); }
	public boolean removeGame(GameHandler game) { return games.remove(game); }

	public void run() throws IOException {
		while (true) {
			Player newPlayer = new Player(this, server.accept());
			new Thread(newPlayer).start();
		}
	}


	public static void main(String[] args) {
		/* TESTING */ args = new String[] { "6969" };

		if (args.length < 1) {
			System.out.println("Usage: java Server <port>");
			return;
		}

		try {
			Server server = new Server(Integer.parseInt(args[0]));
			System.out.println("Started server on port " + args[0]);
			server.run();
		} catch (Exception _e) {}
	}
}

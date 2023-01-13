import java.util.Scanner;
import java.net.*;
import java.io.*;

public class Client {
	private Game game;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public Client(String host, int port) throws Exception {
		this.game = new Game();
		this.socket = new Socket(host, port);
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.out = new PrintWriter(socket.getOutputStream(), true);
	}

	public String[] getResponseData(String responseStr) {
		return responseStr.split(Player.PAYLOAD_DELIMITER);
	}

	public void send(String data) {
		out.println(data);
	}

	public void run() throws Exception {
		Scanner scanner = new Scanner(System.in);

		String responseStr;
		String[] response;

		send(Player.OP_REQUEST_GAME);
		System.out.println("Waiting for game...");

		while ((responseStr = in.readLine()) != null
			&& !responseStr.startsWith(Player.OP_RESPONSE_GAME))
			System.out.println("FIRST: " + responseStr);

		response = getResponseData(responseStr);
		int playerCode = Integer.parseInt(response[1]);
		String playerStr = "X";
		if (playerCode == Game.PLAYER_ONE)
			playerStr = "O";

		System.out.println("Game starting");
		System.out.println("You are " + playerStr);

		while ((responseStr = in.readLine()) != null) {
			System.out.println("SECOND: " + responseStr);
			response = getResponseData(responseStr);
			String opcode = response[0];

			if (opcode.equals(Player.OP_REQUEST_MOVE)) {
				if (response.length > 1) {
					System.out.println(response[1] + ". Try again");
				}

				int moveRow, moveCol;
				while (true) {
					System.out.print("Your move (Row, Col): ");
					String[] move = scanner.nextLine().split(",");
					try {
						moveRow = Integer.parseInt(move[0].trim()) - 1;
						moveCol = Integer.parseInt(move[1].trim()) - 1;
						break;
					} catch (Exception _e) {
						System.out.println("Invalid format. Try again");
					}
				}

				send(Player.OP_REQUEST_MOVE
						+ Player.PAYLOAD_DELIMITER + moveRow
						+ Player.PAYLOAD_DELIMITER + moveCol);
			}

			else if (opcode.equals(Player.OP_RESPONSE_MOVE)) {
				System.out.println("got move thing " + responseStr);
			}

			else if (opcode.equals(Player.OP_RESPONSE_GAME_ENDED)) {
			}
		}
	}

	public static void main(String[] args) {
		/* TESTING */ args = new String[] { "127.0.0.1", "6969" };

		if (args.length < 2) {
			System.out.println("Usage: java Client <host> <port>");
			return;
		}

		try {
			Client client = new Client(args[0], Integer.parseInt(args[1]));
			System.out.println("Connected to server at " + args[0] + ":" + args[1]);
			client.run();
		} catch (Exception _e) {}
	}
}

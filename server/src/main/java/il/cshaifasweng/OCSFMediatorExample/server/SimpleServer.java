package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.BoardMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.GameEndMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.MoveMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;

public class SimpleServer extends AbstractServer {
	private ConnectionToClient playerX;
	private ConnectionToClient playerO;
	private boolean gameInProgress = false;
	private char [][] board = new char[3][3];
	private char currentPlayer = 'X';


	public SimpleServer(int port) {
		super(port);
		resetBoard();
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg instanceof MoveMessage && gameInProgress) {
			MoveMessage move = (MoveMessage) msg;
			handleMove(move, client);
		} else if (msg instanceof String) {
			String msgString = msg.toString();
			System.out.println(msgString);
			if (msgString.startsWith("add player")){
				try {
					System.out.println(playerX);
					System.out.println(playerO);
					if (playerX == null) {
						playerX = client;
						client.sendToClient("client added successfully");
					} else if (playerO == null) {
						playerO = client;
						client.sendToClient("client added successfully");
					}
					if(playerX != null && playerO != null && !gameInProgress) {
						startNewGame();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else if(msgString.startsWith("remove player")){
				System.out.println("remove player");
				System.out.println(gameInProgress);
				if (gameInProgress) {
					try {
						if (playerX == client) {
							System.out.println("Player X left the game");
							playerO.sendToClient(new GameEndMessage("Opponent left!"));
						} else {
							System.out.println("Player O left the game");
							playerX.sendToClient(new GameEndMessage("Opponent left!"));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				gameInProgress = false;
				playerO = null;
				playerX = null;
			}
		}
	}

	private void resetBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = ' ';
			}
		}
		currentPlayer = 'X';
	}


	private void startNewGame() {
		gameInProgress = true;
		resetBoard();
		// Send initial board state to both players
		try {
			playerO.sendToClient(new BoardMessage(board, currentPlayer, 'O'));
			playerX.sendToClient(new BoardMessage(board, currentPlayer, 'X'));
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private void handleMove(MoveMessage move, ConnectionToClient client) {
		int row = move.getRow();
		int col = move.getCol();

		// Determine if the client making the move matches the currentPlayer
		char clientSymbol = (client == playerX) ? 'X' : (client == playerO) ? 'O' : ' ';
		if (clientSymbol == currentPlayer && board[row][col] == ' ') {
			board[row][col] = currentPlayer;
			try {
				switchPlayer();
				playerO.sendToClient(new BoardMessage(board, currentPlayer, 'O'));
				playerX.sendToClient(new BoardMessage(board, currentPlayer, 'X'));
				if (checkWin()) {
					playerO.sendToClient(new GameEndMessage("Player " +  ((currentPlayer == 'X') ? 'O' : 'X') + " wins!"));
					playerX.sendToClient(new GameEndMessage("Player " +  ((currentPlayer == 'X') ? 'O' : 'X') + " wins!"));
					playerO = null;
					playerX = null;
					gameInProgress = false;
				} else if (checkDraw()) {
					playerO.sendToClient(new GameEndMessage("It's a draw!"));
					playerX.sendToClient(new GameEndMessage("It's a draw!"));
					playerO = null;
					playerX = null;
					gameInProgress = false;
				}
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}


	private boolean checkWin() {
		// Check rows
		for (int i = 0; i < 3; i++) {
			if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
				return true;
			}
		}
		// Check columns
		for (int j = 0; j < 3; j++) {
			if (board[0][j] != ' ' && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
				return true;
			}
		}
		// Check diagonals
		if (board[1][1] != ' ' && ((board[0][0] == board[1][1] && board[1][1] == board[2][2]) ||
				(board[2][0] == board[1][1] && board[1][1] == board[0][2]))) {
			return true;
		}
		return false;
	}


	private boolean checkDraw() {
		// A draw occurs if there are no empty cells and there is no winner.
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == ' ') {
					// Found an empty cell, so not a draw yet.
					return false;
				}
			}
		}

		// If we got here, the board is full. If checkWin() was false previously,
		// and no empty spaces remain, then it's a draw.
		return true;
	}

	private void switchPlayer() {
		currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
	}
}

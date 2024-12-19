package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class BoardMessage implements Serializable {
    private char[][] board;
    private char currentPlayer;
    private char selfPlayer;
    public BoardMessage(char[][] board, char currentPlayer, char selfPlayer) {
        this.board = copyBoard(board);
        this.currentPlayer = currentPlayer;
        this.selfPlayer = selfPlayer;
    }
    public char[][] getBoard() { return board; }
    public char getCurrentPlayer() { return currentPlayer; }
    public char getSelfPlayer() { return selfPlayer; }

    private char[][] copyBoard(char[][] source) {
        char[][] copy = new char[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(source[i], 0, copy[i], 0, 3);
        }
        return copy;
    }
}


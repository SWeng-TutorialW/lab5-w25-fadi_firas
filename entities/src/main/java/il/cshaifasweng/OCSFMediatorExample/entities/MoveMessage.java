package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class MoveMessage implements Serializable {
    private int row, col;
    public MoveMessage(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}

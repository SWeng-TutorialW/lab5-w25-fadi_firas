package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class GameEndMessage implements Serializable {
    private String result;
    public GameEndMessage(String result) { this.result = result; }
    public String getResult() { return result; }
}

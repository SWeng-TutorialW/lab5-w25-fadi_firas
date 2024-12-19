package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.BoardMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.MoveMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SecondaryController {

    @FXML
    private Button btn00;

    @FXML
    private Button btn01;

    @FXML
    private Button btn02;

    @FXML
    private Button btn10;

    @FXML
    private Button btn11;

    @FXML
    private Button btn12;

    @FXML
    private Button btn20;

    @FXML
    private Button btn21;

    @FXML
    private Button btn22;

    @FXML
    private Label statusLbl;

    private BoardMessage currentBoard;

    @FXML
    void onGridBtnClick(ActionEvent event) {
        if (((Button) event.getSource()).getText().trim() == "") {
            int row = 0, col = 0;
            if (((Button) event.getSource()) == btn00) {
                row = 0;
                col = 0;
            } else if (((Button) event.getSource()) == btn01) {
                row = 1;
                col = 0;
            } else if (((Button) event.getSource()) == btn02) {
                row = 2;
                col = 0;
            } else if (((Button) event.getSource()) == btn10) {
                row = 0;
                col = 1;
            } else if (((Button) event.getSource()) == btn11) {
                row = 1;
                col = 1;
            } else if (((Button) event.getSource()) == btn12) {
                row = 2;
                col = 1;
            } else if (((Button) event.getSource()) == btn20) {
                row = 0;
                col = 2;
            } else if (((Button) event.getSource()) == btn21) {
                row = 1;
                col = 2;
            } else if (((Button) event.getSource()) == btn22) {
                row = 2;
                col = 2;
            }
            try {
                SimpleClient.getClient().sendToServer(new MoveMessage(row, col));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void initialize(){
        EventBus.getDefault().register(this);
    }

    public void initializeBoard(BoardMessage bm){
        this.currentBoard = bm;
        updateUI();
    }

    public void updateUI(){
        if (currentBoard != null){
             if(currentBoard.getCurrentPlayer() == currentBoard.getSelfPlayer()){
                 toggleAllButtons(true);
                 statusLbl.setText(currentBoard.getCurrentPlayer() + " is playing (You)");
             } else {
                 statusLbl.setText(currentBoard.getCurrentPlayer() + " is playing (Opp)");
                 toggleAllButtons(false);
             }
             char[][] board = currentBoard.getBoard();
             btn00.setText(board[0][0] + "");
             btn01.setText(board[1][0] + "");
             btn02.setText(board[2][0] + "");
             btn10.setText(board[0][1] + "");
             btn11.setText(board[1][1] + "");
             btn12.setText(board[2][1] + "");
             btn20.setText(board[0][2] + "");
             btn21.setText(board[1][2] + "");
             btn22.setText(board[2][2] + "");
        }
    }

    private void toggleAllButtons(boolean toggle){
        btn00.setDisable(!toggle);
        btn01.setDisable(!toggle);
        btn02.setDisable(!toggle);
        btn10.setDisable(!toggle);
        btn11.setDisable(!toggle);
        btn12.setDisable(!toggle);
        btn20.setDisable(!toggle);
        btn21.setDisable(!toggle);
        btn22.setDisable(!toggle);
    }

    @Subscribe
    public void onBoardMessage(BoardMessage bm){
        this.currentBoard = bm;
        Platform.runLater(this::updateUI);
    }
}

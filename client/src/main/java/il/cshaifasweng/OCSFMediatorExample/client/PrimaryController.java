package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class PrimaryController {

	@FXML
	private Button playBtn;

	@FXML
	void initialize(){
		EventBus.getDefault().register(this);
	}

	@FXML
	void startGame(ActionEvent event) {
		playBtn.setDisable(true);
		playBtn.setText("Connecting...");

		try {
			SimpleClient.getClient().sendToServer("add player");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Subscribe
	public void onConnected(ConnectedEvent event) {
		Platform.runLater(() -> {
			playBtn.setText("Searching for opponent...");
		});
	}

}

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.BoardMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.GameEndMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;
    private SimpleClient client;
    private boolean firstBoardMessage;

    @Override
    public void start(Stage stage) throws IOException {
        EventBus.getDefault().register(this);
        firstBoardMessage = true;
    	client = SimpleClient.getClient();
    	client.openConnection();
        App.stage = stage;
        scene = new Scene(loadFXML("primary"));
        stage.setScene(scene);
        stage.show();
    }
    
    @Subscribe
    public void onBoardMessage(BoardMessage message) {
        Platform.runLater(() -> {
            if (firstBoardMessage) {
                firstBoardMessage = false;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
                    Parent root = loader.load();
                    SecondaryController controller = loader.getController();
                    controller.initializeBoard(message);
                    Scene secondScene = new Scene(root);
                    stage.setScene(secondScene);
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Subscribe
    public void onGameEndMessage(GameEndMessage message) {
        System.out.println("Game ended");
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    String.format("Game Ended, %s",
                            message.getResult())
            , ButtonType.OK);
            alert.showAndWait();
            try {
                scene = new Scene(loadFXML("primary"));
                stage.setScene(scene);
                stage.show();
                firstBoardMessage = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
        EventBus.getDefault().unregister(this);
        client.sendToServer("remove player");
        System.out.println("remove player");
        client.closeConnection();
		super.stop();
	}

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


	public static void main(String[] args) {
        launch();
    }

}
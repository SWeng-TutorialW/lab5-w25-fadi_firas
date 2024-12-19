package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.BoardMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.GameEndMessage;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		if (msg instanceof BoardMessage) {
			EventBus.getDefault().post(msg);
		} else if (msg instanceof GameEndMessage) {
			EventBus.getDefault().post(msg);
		} else {
			String message = msg.toString();
			if (message.equals("client added successfully")) {
				EventBus.getDefault().post(new ConnectedEvent());
			}
			System.out.println(message);
		}
	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}

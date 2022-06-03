import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
Gson parser = new Gson();

public class WebSocket extends WebSocketClient {
  public WebSocket(URI serverUri) {
    super(serverUri);
  }

  @Override
    public void onMessage(String message) {
      if(!message.startsWith("{")) {
      System.out.println("[CLIENT] Recieved Message: " + message);
      return;
      }
    Packet p = parser.fromJson(message, Packet.class);
    if (p.type.equals("missile")) {
      sendHit(p.missile);
    }
    if (p.type.equals("status") && p.status.status.equals("GAME_END")) {
      setResults(p.status.bool);
    }
  }

  @Override
    public void onError(Exception e) {
    System.out.println("[CLIENT] Error:" + e);
  }

  @Override
    public void onClose(int code, String reason, boolean remote) {
    System.out.println( "You have been disconnected from the server Code: " + code + " " + reason);
  }

  @Override
    public void onOpen(ServerHandshake handshake) {
    System.out.println("Client now connected to server.");
  }
}
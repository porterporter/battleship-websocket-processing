import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

Gson parser = new Gson();

public class Server extends WebSocketServer {
  WebSocket client;

  public Server (InetSocketAddress address) {
    super(address);

    try {
      System.out.println("Your IP: " + InetAddress.getLocalHost().getHostAddress());
    } 
    catch (Exception e) {
    }
  }

  @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    conn.send("Welcome to the server!"); //This method sends a message to the new client
    System.out.println(
      conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
    client = conn;
  }

  @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    broadcast(conn + " has left the room!");
    System.out.println(conn + " has left the room!");
  }

  @Override
    public void onMessage(WebSocket conn, String message) {
    Packet p = parser.fromJson(message, Packet.class);
    if (p.type.equals("missile")) {
      sendHit(p.missile);
    }
    if (p.type.equals("status") && p.status.status.equals("GAME_END")) {
      setResults(p.status.bool);
    }
  }


  @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
    broadcast(message.array());
    System.out.println(conn + ": " + message);
  }

  @Override
    public void onError(WebSocket conn, Exception ex) {
    ex.printStackTrace();
    if (conn != null) {
      // some errors like port binding failed may not be assignable to a specific websocket
    }
  }

  @Override
    public void onStart() {
    System.out.println("Server started!");
    setConnectionLostTimeout(0);
    setConnectionLostTimeout(100);
  }

  public WebSocket getClient() {
    return client;
  }
}
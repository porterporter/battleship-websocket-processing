import java.net.InetSocketAddress;
import org.java_websocket.client.WebSocketClient;

Server server;

ArrayList<Square> squares;
String shownText = "";
String state = "setup";
boolean myTurn = false;
int selectedSquares = 0;
int aliveSquares = 5;

Gson builder = new GsonBuilder().serializeNulls().create();

void setup() {
  size(460, 460);
  server = new Server(new InetSocketAddress(1001));
  server.start();
  textAlign(CENTER);
  background(230);

  setupBoard();
}

void draw() {
  background(230);
  strokeWeight(4);

  for (Square s : squares) {
    if (s.getSent()) {
      stroke(255);
    } else {
      stroke(0);
    }

    if (!s.alive) {
      fill(255, 110, 110);
    } else if (s.occupied) {
      fill(198, 198, 198);
    } else {
      fill(88, 88, 88);
    }

    rect(10 * (s.x + 1) + s.x * 80, 10 * (s.y + 1) + s.y * 80, 80, 80);
  }

  noStroke();
  textSize(70);
  fill(255, 255, 255);
  text(shownText, 250, 250);
}


void setupBoard() {
  shownText = "Pick 5 Squares";
  for (int turns = 0; turns < 5; turns++) {
  }
  squares = new ArrayList();
  for (int r = 0; r < 5; r++) {
    for (int c = 0; c < 5; c++) {
      squares.add(new Square(r, c, false));
    }
  }
}

void sendHit(Missile m) {
  if (state.equals("game")) {
    Square square = null;
    for (Square s : squares) {
      if (s.coordsEqual(m.x, m.y)) {
        square = s;
        break;
      }
    }

    if (square == null) {
      shownText = "NULL SQUARE! RESTART GAME!";
      return;
    }

    myTurn = true;

    boolean hit = square.hit(m);
    if (hit) aliveSquares--;
    if (aliveSquares < 1) setResults(false);
  }
}


void mouseClicked() {
  if (state.equals("setup")) {

    int x = -1, y = -1;
    println(mouseX, mouseY);

    for (int r = 0; r < 5; r++) {
      for (int c = 0; c < 5; c++) {
        if (mouseX >= 10 * r + r * 80 &&
          mouseX <= 10 * r + r * 80 + 80 &&
          mouseY >= 10 * c + c * 80 &&
          mouseY <= 10 * c + c * 80 + 80) {
          x = r;
          y = c;
        }
      }
    }
    if (x > -1 && y > -1) {
      println("Clicked at: ", x, y);
      for (Square s : squares) {
        if (s.coordsEqual(x, y) && s.occupied == false) {
          s.occupied = true;
          selectedSquares++;
          if (selectedSquares == 5) {
            state = "game";
            shownText = "";
            return;
          }
        }
      }
    }
  }



  if (state.equals("game") && myTurn) {
    int x = -1, y = -1;
    for (int r = 0; r < 5; r++) {
      for (int c = 0; c < 5; c++) {
        if (mouseX >= 10 * r + r * 80 &&
          mouseX <= 10 * r + r * 80 + 80 &&
          mouseY >= 10 * c + c * 80 &&
          mouseY <= 10 * c + c * 80 + 80) {
          x = r;
          y = c;
        }
      }
    }

    if (x > -1 && y > -1) {

      for (Square s : squares) {
        if (s.coordsEqual(x, y)) {
          s.setSent(true);
          break;
        }
      }

      Packet p = new Packet(new Missile(x, y));
      server.getClient().send(builder.toJson(p));
      myTurn = false;
    }
  }
}

void setResults(boolean win) {
  state = "finished";
  if (win) {
    shownText = "You won!";
  } else {
    shownText = "You lost!";
    Packet p = new Packet(new Status("GAME_END", true));
    server.getClient().send(builder.toJson(p));
  }
}
public class Packet {
    String type;
    Missile missile;
    Status status;
    public Packet(Missile m) {
      this.type = "missile";
      this.missile = m;
      this.status = null;
    }
    public Packet(Status s) {
      this.type = "status";
      this.status = s;
      this.missile = null;
    }
  }
  
  public class Missile {
    int x;
    int y;
  
    Missile(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }
  
  public class Status {
    String status;
    boolean bool;
    Status(String status, boolean bool) {
      this.status = status;
      this.bool = bool;
    }
    Status(String status) {
      this(status, true);
    }
  }
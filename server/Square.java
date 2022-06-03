public class Square {
    public int x, y;
    public boolean alive;
    public boolean occupied;
    public boolean sent;
  
    public Square(int x, int y, boolean occupied) {
      this.x = x;
      this.y = y;
      this.occupied = occupied;
      this.alive = true;
      this.sent = false;
    }
  
    public boolean hit(Missile m) {
      if (this.x == m.x && this.y == m.y && this.occupied) {
        this.alive = false;
        return true;
      }
      return false;
    }
  
    public boolean getSent() {
      return this.sent;
    }
  
    public void setSent(boolean b) {
      this.sent = b;
    }
  
    public boolean coordsEqual(int x, int y) {
      return this.x == x && this.y == y;
    }
    
    
  }
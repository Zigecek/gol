package xyz.kozohorsky.gol;

public class GameUnit {
  private boolean alive = false;
  public void born() {
    this.alive = true;
  };
  public void die() {
    this.alive = false;
  };
  public boolean isAlive() {
    return this.alive;
  };
}

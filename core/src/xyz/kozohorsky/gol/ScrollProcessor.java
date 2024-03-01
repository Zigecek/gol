package xyz.kozohorsky.gol;

import com.badlogic.gdx.InputProcessor;

public class ScrollProcessor implements InputProcessor {

  public int scrollAmount = 0;
  @Override
  public boolean keyDown(int i) {
    return false;
  }

  @Override
  public boolean keyUp(int i) {
    return false;
  }

  @Override
  public boolean keyTyped(char c) {
    return false;
  }

  @Override
  public boolean touchDown(int i, int i1, int i2, int i3) {
    return false;
  }

  @Override
  public boolean touchUp(int i, int i1, int i2, int i3) {
    return false;
  }

  @Override
  public boolean touchCancelled(int i, int i1, int i2, int i3) {
    return false;
  }

  @Override
  public boolean touchDragged(int i, int i1, int i2) {
    return false;
  }

  @Override
  public boolean mouseMoved(int i, int i1) {
    return false;
  }

  @Override
  public boolean scrolled(float v, float v1) {
    scrollAmount += (int) v1;
    return false;
  }
}
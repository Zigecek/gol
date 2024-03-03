package xyz.kozohorsky.gol.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class GolStage extends Stage {

  public GolStage() {
    super();
  }

  public GolStage(Viewport viewport) {
    super(viewport);
  }

  @Override
  public OrthographicCamera getCamera() {
    return (OrthographicCamera) super.getCamera();
  }

  public void set(int x, int y, int width, int height) {
    getViewport().update(width, height);
    getViewport().setScreenBounds(x, y, width, height);
  }

  public Vector3 getScreenBottomLeft() {
    return new Vector3(getViewport().getScreenX(), Gdx.graphics.getHeight() - getViewport().getScreenY(), 0);
  }

  public Vector3 getScreenTopRight() {
    return new Vector3(getViewport().getScreenX() + getViewport().getScreenWidth(), Gdx.graphics.getHeight() - (getViewport().getScreenY() + getViewport().getScreenHeight()), 0);
  }

}

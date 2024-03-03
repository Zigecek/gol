package xyz.kozohorsky.gol.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import xyz.kozohorsky.gol.layout.Layoutable;

import java.lang.reflect.Field;

public abstract class LayoutStage extends Stage implements Layoutable {
  private final int scaling;

  static LayoutStage activeStage;

  public LayoutStage(Viewport viewport, int scaling) {
    super(viewport);
    this.scaling = scaling;
  }

  @Override
  public int getScaling() {
    return scaling;
  }

  public abstract void initLayout();

  @Override
  public OrthographicCamera getCamera() {
    return (OrthographicCamera) super.getCamera();
  }

  @Override
  public void act() {
    if (anyMouseButtonJustPressed() && isInsideViewport(Gdx.input.getX(), Gdx.input.getY())) {
      activeStage = this;
    }
    super.act(Gdx.graphics.getDeltaTime());
  }

  private boolean anyMouseButtonJustPressed() {
    for (Field field : Input.Buttons.class.getDeclaredFields()) {
      try {
        if (Gdx.input.isButtonJustPressed(field.getInt(null))) {
          return true;
        }
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
    return false;
  }

  @Override
  public void set(int x, int y, int width, int height) {
    getViewport().setWorldSize(width, height);
    getViewport().setScreenBounds(x, y, width, height);
    getViewport().apply();
  }

  public Vector3 getScreenBottomLeft() {
    return new Vector3(getViewport().getScreenX(), Gdx.graphics.getHeight() - getViewport().getScreenY(), 0);
  }

  public Vector3 getScreenTopRight() {
    return new Vector3(getViewport().getScreenX() + getViewport().getScreenWidth(), Gdx.graphics.getHeight() - (getViewport().getScreenY() + getViewport().getScreenHeight()), 0);
  }

  @Override
  public void draw() {
    getViewport().apply();
    super.draw();
  }
}

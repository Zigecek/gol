package xyz.kozohorsky.gol.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import xyz.kozohorsky.gol.layout.Layoutable;

import java.lang.reflect.Field;

public abstract class LayoutStage extends GolStage implements Layoutable {
  private int scaling;

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
  public void set(int x, int y, int width, int height) {
    System.out.println("Setting layout stage to " + width + "x" + height);
    super.set(x, y, width, height);
  }

  @Override
  public void act() {
    if (anyMouseButtonJustPressed() && isInsideViewport(Gdx.input.getX(), Gdx.input.getY())) {
      activeStage = this;
    }
    super.act();
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
  public void draw() {
    getViewport().apply();
    super.draw();
  }
}

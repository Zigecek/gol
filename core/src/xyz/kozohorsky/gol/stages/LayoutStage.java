package xyz.kozohorsky.gol.stages;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import xyz.kozohorsky.gol.layout.Layoutable;

public abstract class LayoutStage extends GolStage implements Layoutable {
  private int scaling;

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
}

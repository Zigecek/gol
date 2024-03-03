package xyz.kozohorsky.gol.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class ConfigurationStage extends LayoutStage {
  public final static int WORLD_WIDTH = 100; // doesn't matter
  public final static int WORLD_HEIGHT = 100; // doesn't matter

  public ConfigurationStage(int scaling) {
    super(
      new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT,
        new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT)
      ),
      scaling
    );
  }

  public ConfigurationStage() {
    this(-1);
  }

  @Override
  public void draw() {
    super.draw();
  }

  @Override
  public void act() {
    super.act();
  }

  @Override
  public void dispose() {
    super.dispose();
  }

  @Override
  public void initLayout() {
    getCamera().position.set(getCamera().viewportWidth / 2, getCamera().viewportHeight / 2, 0);
  }
}

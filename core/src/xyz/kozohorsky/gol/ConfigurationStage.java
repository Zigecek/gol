package xyz.kozohorsky.gol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class ConfigurationStage extends GolStage {
  public final static int WORLD_WIDTH = 100; // doesn't matter
  public final static int WORLD_HEIGHT = 100; // doesn't matter
  //private final OrthographicCamera cam;

  public ConfigurationStage() {
    super(
      new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT,
        new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT)
      )
    );
    getCamera().position.set(getCamera().viewportWidth / 2, getCamera().viewportHeight / 2, 0);
  }

  @Override
  public void draw() {
    super.draw();
  }

  @Override
  public void act(float delta) {
    super.act(delta);
  }

  @Override
  public void setViewportToScreen() {
    getViewport().setScreenBounds(0, 0, Gdx.graphics.getWidth() /*/ 2*/, Gdx.graphics.getHeight());
  }

  @Override
  public void dispose() {
    super.dispose();
  }
}

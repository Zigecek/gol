package xyz.kozohorsky.gol;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class GolStage extends Stage {
  public GolStage(Viewport viewport) {
    super(viewport);
  }

  @Override
  public OrthographicCamera getCamera() {
    return (OrthographicCamera) super.getCamera();
  }

  public void updateViewport(int width, int height) {
    getViewport().update(width, height);

    setViewportToScreen();
  }

  public abstract void setViewportToScreen();
}

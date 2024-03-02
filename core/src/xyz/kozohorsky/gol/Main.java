package xyz.kozohorsky.gol;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import xyz.kozohorsky.gol.layout.Column;
import xyz.kozohorsky.gol.layout.Row;
import xyz.kozohorsky.gol.stages.*;

public class Main extends ApplicationAdapter {
  private Main() {
  }

  private static Main instance;

  public static Main get() {
    if (instance == null) {
      instance = new Main();
    }
    return instance;
  }

  private StageManager stageManager;

  @Override
  public void create() {
    stageManager = new StageManager(
      new Row(
        new Column(
          new GameStage(),
          new GameStage()
        ),
        new Column(
          new GameStage(),
          new GameStage()
        )
      )
    );
    Gdx.input.setInputProcessor(stageManager.getInputProcessor());

    Main.get().resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    for (LayoutStage stage : stageManager.getStages()) {
      stage.initLayout();
    }
  }

  @Override
  public void render() {
    ScreenUtils.clear(Color.BLACK, true);

    stageManager.render();
  }

  @Override
  public void resize(int width, int height) {
    System.out.println("Resized to " + width + "x" + height);

    stageManager.updateViewport(width, height);
  }

  @Override
  public void dispose() {
    stageManager.dispose();
  }
}

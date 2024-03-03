package xyz.kozohorsky.gol;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import xyz.kozohorsky.gol.layout.Row;
import xyz.kozohorsky.gol.stages.ConfigurationStage;
import xyz.kozohorsky.gol.stages.GameStage;
import xyz.kozohorsky.gol.stages.LayoutStage;
import xyz.kozohorsky.gol.stages.StageManager;

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
        new GameStage(5),
        new ConfigurationStage(3)
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
    if (width < 725 || height < 450) {
      Gdx.graphics.setWindowedMode(725, 450);
      stageManager.updateViewport(725, 450);
      return;
    }
    stageManager.updateViewport(width, height);
  }

  @Override
  public void dispose() {
    stageManager.dispose();
  }
}

package xyz.kozohorsky.gol;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
  private Main () {
  }
  private static Main instance;
  public static Main get() {
    if (instance == null) {
      instance = new Main();
    }
    return instance;
  }
  private GameStage gameStage;
  private ConfigurationStage configurationStage;
  @Override
  public void create() {
    gameStage = new GameStage();
    configurationStage = new ConfigurationStage();

    Gdx.input.setInputProcessor(new InputMultiplexer(gameStage, configurationStage));
  }

  @Override
  public void render() {
    ScreenUtils.clear(Color.BLACK, true);

    gameStage.act();
    gameStage.draw();

//    configurationStage.act();
//    configurationStage.draw();
  }

  @Override
  public void resize(int width, int height) {
    gameStage.updateViewport(width, height);
    configurationStage.updateViewport(width, height);
  }

  @Override
  public void dispose() {
    gameStage.dispose();
    configurationStage.dispose();
  }
}

package xyz.kozohorsky.gol.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import xyz.kozohorsky.gol.utils.GameLogic;

public class ConfigurationStage extends LayoutStage {
  private final ShapeRenderer shapeRenderer;
  private final VisTable table;
  private final VisSlider delaySlider;
  private final VisLabel delayValue;
  private final VisTextButton fillRandomButton;
  private final VisTextButton clearButton;
  private final VisTextButton pauseButton;
  private final VisTextButton newGenerationButton;
  private final VisLabel delayLabel;

  public ConfigurationStage(int scaling) {
    super(new ScreenViewport(new OrthographicCamera()), scaling);

    VisUI.load();

    shapeRenderer = new ShapeRenderer();
    table = new VisTable(true);
    this.addActor(table);
    table.setClip(true);

    delaySlider = new VisSlider(100, 2000, 100, false);
    delayValue = new VisLabel("0");
    fillRandomButton = new VisTextButton("Fill Random");
    clearButton = new VisTextButton("Clear");
    pauseButton = new VisTextButton("Pause");
    newGenerationButton = new VisTextButton("New Generation");
    delayLabel = new VisLabel("Delay:");

    pauseButton.setColor(Color.SLATE);


    delaySlider.addListener(new ClickListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        setDelay((int) delaySlider.getValue());
        return true;
      }
    });
    fillRandomButton.addListener(new ClickListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        for (GameStage gameStage : GameStage.instances) {
          gameStage.fillRandom();
        }
        return true;
      }
    });
    clearButton.addListener(new ClickListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        for (GameLogic gameLogic : GameLogic.instances) {
          gameLogic.aliveCells.clear();
        }
        return true;
      }

    });
    pauseButton.addListener(new ClickListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        for (GameLogic gameLogic : GameLogic.instances) {
          gameLogic.isInEditMode = !gameLogic.isInEditMode;
        }
        return true;
      }
    });
    newGenerationButton.addListener(new ClickListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        for (GameLogic gameLogic : GameLogic.instances) {
          gameLogic.nextGeneration();
        }
        return true;
      }
    });
  }

  public ConfigurationStage() {
    this(-1);
  }

  @Override
  public void draw() {
    super.draw();
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    shapeRenderer.setColor(Color.DARK_GRAY);
    shapeRenderer.rect(0, 0, getCamera().viewportWidth, getCamera().viewportHeight);
    shapeRenderer.end();

    getBatch().begin();
    table.draw(getBatch(), 1);
    getBatch().end();
  }

  @Override
  public void act() {
    super.act();
    getBatch().setProjectionMatrix(getCamera().combined);
    shapeRenderer.setProjectionMatrix(getCamera().combined);

    getCamera().position.set(getCamera().viewportWidth / 2, getCamera().viewportHeight / 2, 0);
    table.invalidateHierarchy();
    table.setSize(getViewport().getScreenWidth(), getViewport().getScreenHeight());
    table.validate();
    table.clear();

    delaySlider.setValue(getDelay());
    delayValue.setText(String.valueOf(getDelay()));

    pauseButton.setText(GameLogic.instances.get(0).isInEditMode ? "Resume" : "Pause");

    table.top().left().pad(5);
    table.row().expand();
    table.add(delayLabel).pad(5);
    table.add(delayValue).pad(5);
    table.add(delaySlider).pad(5).fill().expand();
    table.row();
    table.add(pauseButton).colspan(10).pad(5).fill().expand();
    table.row();
    table.add(fillRandomButton).colspan(10).pad(5).fill().expand();
    table.row();
    table.add(clearButton).colspan(10).pad(5).fill().expand();
    table.row();
    table.add(newGenerationButton).colspan(10).pad(5).fill().expand();

    table.act(Gdx.graphics.getDeltaTime());
  }

  private int getDelay() {
    return GameLogic.instances.get(0).delay;
  }

  private void setDelay(int delay) {
    for (GameLogic gameLogic : GameLogic.instances) {
      gameLogic.delay = delay;
    }
  }

  @Override
  public void dispose() {
    super.dispose();
    VisUI.dispose();
  }

  @Override
  public void initLayout() {
    getCamera().position.set(getCamera().viewportWidth / 2, getCamera().viewportHeight / 2, 0);
  }
}
package xyz.kozohorsky.gol.stages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class ConfigurationStage extends LayoutStage {
  private ShapeRenderer shapeRenderer;
  private VisTable table;

  public ConfigurationStage(int scaling) {
    super(
      new ScreenViewport(
        new OrthographicCamera()
      ),
      scaling
    );

    VisUI.load();

    shapeRenderer = new ShapeRenderer();
    table = new VisTable(true);
    //table.setFillParent(true);
    this.addActor(table);
    table.setDebug(true);

    // Vytvoříme posuvník s číselným vstupem
    VisSlider delaySlider = new VisSlider(0, 100, 1, false);
    VisLabel delayValue = new VisLabel("0");
    delaySlider.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        delayValue.setText(String.valueOf(((VisSlider) actor).getValue()));
      }
    });

    VisTextButton fillRandomButton = new VisTextButton("Fill Random");
    VisTextButton clearButton = new VisTextButton("Clear");
    VisTextButton pauseButton = new VisTextButton("Pause");
    VisTextButton newGenerationButton = new VisTextButton("New Generation");

    fillRandomButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        // Zde přidejte kód pro fillRandom
      }
    });

    clearButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        // Zde přidejte kód pro clear
      }
    });

    pauseButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        // Zde přidejte kód pro pause
      }
    });

    newGenerationButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        // Zde přidejte kód pro new generation
      }
    });


    table.top().left().pad(10); // Zarovnání a padding
    table.add(new VisLabel("Delay:")).pad(10);
    table.add(delaySlider).pad(10).fillX(); // Šířka posuvníku
    table.add(delayValue).pad(10);
    table.row();
    table.add(fillRandomButton).colspan(3).pad(10).fillX();
    table.row();
    table.add(clearButton).colspan(3).pad(10).fillX();
    table.row();
    table.add(pauseButton).colspan(3).pad(10).fillX();
    table.row();
    table.add(newGenerationButton).colspan(3).pad(10).fillX();
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
package xyz.kozohorsky.gol;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

public class Gol extends ApplicationAdapter {
  public static final long SLEEP_TIME = 500;
  public static final int UNIT_SIZE = 20;
  public static final int UNITS_PER_SIDE = 200;
  public static final int WIDTH = UNIT_SIZE * UNITS_PER_SIDE;
  public static final int HEIGHT = WIDTH;
  private SpriteBatch batch;
  private OrthographicCamera camera;
  private ShapeRenderer shapeRenderer;
  private BitmapFont font;

  private final Thread loopThread = new Thread(() -> {
    while (true) {
      try {
        Thread.sleep(SLEEP_TIME);
      } catch (InterruptedException e) {
        break;
      }

      // Loop content
      if (!Game.get().isInEditMode) {
        Game.get().nextGeneration();
      }
    }
  });

  @Override
  public void create() {
    batch = new SpriteBatch();
    camera = new OrthographicCamera();
    camera.setToOrtho(false, WIDTH, HEIGHT);

    shapeRenderer = new ShapeRenderer();

    batch.setProjectionMatrix(camera.combined);
    shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OptimusPrinceps.ttf"));
    FreeTypeFontParameter parameter = new FreeTypeFontParameter();
    parameter.size = 130;
    parameter.color = Color.RED;
    font = generator.generateFont(parameter);
    generator.dispose();

    // Computing loop
    loopThread.start();
  }

  @Override
  public void render() {
    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
      Game.get().isInEditMode = !Game.get().isInEditMode;
    }

    if (Game.get().isInEditMode) {
      if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
        Game.get().fillRandom();
      }
      if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
        Game.get().reset();
      }
      if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
        Vector3 clickCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(clickCoordinates);
        int x = (int) (clickCoordinates.x / UNIT_SIZE);
        int y = (int) (clickCoordinates.y / UNIT_SIZE);
        Game.get().toggleUnit(x, y);
      }
    }
    Color colorAlive;
    if (Game.get().isInEditMode) {
      colorAlive = Color.LIME;
    } else {
      colorAlive = Color.WHITE;
    }

    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    for (int y = 0; y < UNITS_PER_SIDE; y++) {
      for (int x = 0; x < UNITS_PER_SIDE; x++) {
        shapeRenderer.setColor(Game.get().isAlive(x, y) ? colorAlive : Color.BLACK);
        shapeRenderer.rect(x * UNIT_SIZE, y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
      }
    }
    shapeRenderer.end();


    if (!Game.get().isGenerationAlive() && !Game.get().isInEditMode) {
      batch.begin();
      GlyphLayout glyphLayout = new GlyphLayout();
      glyphLayout.setText(font, "YOU DIED", Color.RED, WIDTH, Align.center, true);
      font.draw(
        batch,
        "YOU DIED",
        0,
        HEIGHT / 2f + glyphLayout.height / 2f,
        WIDTH,
        Align.center,
        true);
      batch.end();
    }
  }

  @Override
  public void dispose() {
    batch.dispose();
    loopThread.interrupt();
  }

  @Override
  public void resize(int width, int height) {
    if (width != height) {
      int average = (width + height) / 2;
      Gdx.graphics.setWindowedMode(average, average);
    }
  }
}

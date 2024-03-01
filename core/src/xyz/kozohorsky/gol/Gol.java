package xyz.kozohorsky.gol;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;

public class Gol extends ApplicationAdapter {
  public static final long SLEEP_TIME = 500;
  public static final int cellSize = 10;
  private SpriteBatch batch;
  private OrthographicCamera camera;
  private ShapeRenderer shapeRenderer;
  private ScrollProcessor scrollProcessor;
  public float cameraZoom = 1.0f;

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
    camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    shapeRenderer = new ShapeRenderer();
    shapeRenderer.setColor(Color.WHITE);

    batch.setProjectionMatrix(camera.combined);
    shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

    scrollProcessor = new ScrollProcessor();
    Gdx.input.setInputProcessor(scrollProcessor);

    // Computing loop
    loopThread.start();
  }

  @Override
  public void render() {
    ScreenUtils.clear(Color.BLACK, true);

    // Camera zoom
    cameraZoom = Math.max(cameraZoom + scrollProcessor.scrollAmount * cameraZoom * 0.1f, 0.1f);
    scrollProcessor.scrollAmount = 0;
    camera.viewportWidth = (int) (Gdx.graphics.getWidth() * cameraZoom);
    camera.viewportHeight = (int) (Gdx.graphics.getHeight() * cameraZoom);
    camera.update();
    shapeRenderer.setProjectionMatrix(camera.combined);

    keyActions();

    // Rendering of the game cells
    if (Game.get().isInEditMode) {
      shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    } else {
      shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    }
    for (Point cell : Game.get().aliveCells) {
      // these are real coordinates on the batch
      shapeRenderer.rect(
        cell.x * cellSize,
        cell.y * cellSize,
        cellSize,
        cellSize);
    }
    shapeRenderer.end();
  }

  private void keyActions() {
    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
      Game.get().isInEditMode = !Game.get().isInEditMode;
    }

    // dragging camera
    if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
      if (Gdx.input.isTouched()) {
        camera.translate(-Gdx.input.getDeltaX()*cameraZoom, Gdx.input.getDeltaY()*cameraZoom);
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
      }
    }

    // edit mode actions
    if (Game.get().isInEditMode) {
      // R - fill random
      if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
        // calculate x, y of the cells in camera's viewport (bottom left and top right corners) take into account the zoom and the camera position

        Vector3 from = new Vector3(0, Gdx.graphics.getHeight(), 0);
        camera.unproject(from);
        from.y /= cellSize;
        from.x /= cellSize;

        Vector3 to = new Vector3(Gdx.graphics.getWidth(), 0, 0);
        camera.unproject(to);
        to.y /= cellSize;
        to.x /= cellSize;

        Game.get().fillRandom(from, to);
      }
      // C - clear
      if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
        Game.get().aliveCells.clear();
        System.out.println("Should be cleared");
      }

      // mouse click actions
      if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) || Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
        Vector3 clickCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(clickCoordinates);
        int x = (int) (clickCoordinates.x / cellSize);
        int y = (int) (clickCoordinates.y / cellSize);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
          // LEFT CLICK - set cell alive
          Game.get().setCellAlive(x, y);
          System.out.println("coords: " + x + " " + y);
        } else {
          // RIGHT CLICK - set cell dead
          Game.get().setCellDead(x, y);
        }
      }
    }
  }

/*
  @Override
  public void resize(int width, int height) {
    camera.setToOrtho(false, width, height);
    camera.update();
    shapeRenderer.setProjectionMatrix(camera.combined);
  }
*/
  @Override
  public void dispose() {
    batch.dispose();
    loopThread.interrupt();
  }
}

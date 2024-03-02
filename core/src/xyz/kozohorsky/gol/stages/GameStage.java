package xyz.kozohorsky.gol.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.*;
import xyz.kozohorsky.gol.utils.GameLogic;

import java.awt.*;

public class GameStage extends LayoutStage {
  public final static int WORLD_WIDTH = 100; // doesn't matter
  public final static int WORLD_HEIGHT = 100; // doesn't matter
  public final static int CELL_SIZE = 1;
  private final GameLogic gameLogic;
  private final ShapeRenderer shapeRenderer;

  public GameStage(int scaling) {
    super(
      new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT,
        new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT)
      ),
      scaling
    );

    shapeRenderer = new ShapeRenderer();
    shapeRenderer.setColor(Color.WHITE);

    gameLogic = new GameLogic();
  }

  public GameStage() {
    this(-1);
  }

  @Override
  public void act(float delta) {
    getViewport().apply();
    shapeRenderer.setProjectionMatrix(getCamera().combined);

    inputActions();

    // Next generation computation
    if (System.nanoTime() - gameLogic.getLastGenerationTime() >= gameLogic.delay * 1000000 &&
      !gameLogic.isInEditMode) {
      gameLogic.nextGeneration();
    }
  }

  @Override
  public void draw() {
    if (gameLogic.isInEditMode) {
      shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    } else {
      shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    }
    for (Point cell : gameLogic.aliveCells) {
      // these are real coordinates on the batch
      shapeRenderer.rect(
        cell.x,
        cell.y,
        CELL_SIZE,
        CELL_SIZE);
    }
    shapeRenderer.end();
  }

  private void inputActions() {
    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
      gameLogic.isInEditMode = !gameLogic.isInEditMode;
    }

    // dragging camera
    if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
      if (Gdx.input.isTouched()) {
        getCamera().translate(
          -Gdx.input.getDeltaX() * 0.25f * getCamera().zoom,
          Gdx.input.getDeltaY() * 0.25f * getCamera().zoom);
      }
    }

    // next generation manually
    if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
      System.out.println("Force next generation");
      gameLogic.nextGeneration();
    }

    // edit mode actions
    if (gameLogic.isInEditMode) {
      // R - fill random
      if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
        // calculate x, y of the cells in camera's viewport (bottom left and top right corners) take into account the zoom and the camera position

        fillRandom();
      }
      // C - clear
      if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
        gameLogic.aliveCells.clear();
        System.out.println("Should be cleared");
      }

      // mouse click actions
      if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) || Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
        if (!isInsideViewport(Gdx.input.getX(), Gdx.input.getY())) return;

        Vector3 clickCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        getViewport().unproject(clickCoordinates);

        int x = (int) Math.floor(clickCoordinates.x / CELL_SIZE);
        int y = (int) Math.floor(clickCoordinates.y / CELL_SIZE);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
          // LEFT CLICK - set cell alive
          gameLogic.setCellAlive(x, y);
        } else {
          // RIGHT CLICK - set cell dead
          gameLogic.setCellDead(x, y);
        }
      }
    }
  }

  private void fillRandom() {
    if (gameLogic == null) return;

    System.out.println("Viewport size " + getViewport().getScreenWidth() + "x" + getViewport().getScreenHeight());

    gameLogic.fillRandom(
      getViewport().unproject(getScreenBottomLeft()),
      getViewport().unproject(getScreenTopRight())
    );
  }

  @Override
  public boolean scrolled(float amountX, float amountY) {
    getCamera().zoom = Math.max(getCamera().zoom * (1 + amountY * 0.1f), 0.1f);
    return super.scrolled(amountX, amountY);
  }

  @Override
  public void initLayout() {
    //getCamera().position.set(getCamera().viewportWidth / 2, getCamera().viewportHeight / 2, 0);
    fillRandom();
  }

  @Override
  public void dispose() {
    super.dispose();
    shapeRenderer.dispose();
  }
}

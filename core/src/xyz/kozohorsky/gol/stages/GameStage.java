package xyz.kozohorsky.gol.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.*;
import xyz.kozohorsky.gol.utils.GameLogic;

import java.awt.*;

public class GameStage extends LayoutStage {
  private final GameLogic gameLogic;
  private final ShapeRenderer shapeRenderer;

  public GameStage(int scaling) {
    super(
      new ExtendViewport(50,0,
        new OrthographicCamera()
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
  public void act() {
    super.act();
    shapeRenderer.setProjectionMatrix(getCamera().combined);

    keyActions();
    mouseActions();

    // Next generation computation
    if (System.nanoTime() - gameLogic.getLastGenerationTime() >= gameLogic.delay * 1000000 &&
      !gameLogic.isInEditMode) {
      gameLogic.nextGeneration();
    }
  }

  @Override
  public void draw() {
    super.draw();
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
        1,
        1);
    }
    shapeRenderer.end();
  }

  private void keyActions() {
    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
      gameLogic.isInEditMode = !gameLogic.isInEditMode;
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
        fillRandom();
      }
      // C - clear
      if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
        gameLogic.aliveCells.clear();
        System.out.println("Should be cleared");
      }
    }
  }

  private void mouseActions() {
    float factor = getCamera().zoom * 0.4f;
    // dragging camera
    if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
      // check if the first touch is inside the viewport
      if (activeStage != this) return;

      if (Gdx.input.isTouched()) {
        getCamera().translate(
          -Gdx.input.getDeltaX() * factor,
          Gdx.input.getDeltaY() * factor
        );
      }
    }

    if (gameLogic.isInEditMode) {
      if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) || Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
        if (activeStage != this) return;

        Vector3 clickCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        getViewport().unproject(clickCoordinates);

        int x = (int) Math.floor(clickCoordinates.x);
        int y = (int) Math.floor(clickCoordinates.y);

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
    if (!isInsideViewport(Gdx.input.getX(), Gdx.input.getY())) return super.scrolled(amountX, amountY);
    getCamera().zoom = Math.max(getCamera().zoom * (1 + amountY * 0.1f), 0.1f);
    System.out.println("Zoomed to " + getCamera().zoom);
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

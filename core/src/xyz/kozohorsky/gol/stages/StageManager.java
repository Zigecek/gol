package xyz.kozohorsky.gol.stages;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import xyz.kozohorsky.gol.layout.LayoutElement;
import xyz.kozohorsky.gol.layout.Layoutable;

import java.util.ArrayList;

public class StageManager extends Stage {
  ArrayList<LayoutStage> stages = new ArrayList<>();
  ArrayList<Layoutable> layoutElements = new ArrayList<>();

  public StageManager(Layoutable inputLayoutable) {
    super();
    recursivelyAddStages(inputLayoutable);
  }

  public ArrayList<LayoutStage> getStages() {
    return stages;
  }

  private void recursivelyAddStages(Layoutable inputLayoutable) {
    if (inputLayoutable instanceof LayoutStage) {
      stages.add((LayoutStage) inputLayoutable);
    } else if (inputLayoutable instanceof LayoutElement) {
      layoutElements.add(inputLayoutable);
      for (Layoutable layoutable : ((LayoutElement) inputLayoutable).getElements()) {
        recursivelyAddStages(layoutable);
      }
    }
  }

  public void render() {/*
    for (LayoutStage stage : stages) {
      new Thread(stage).start();
    }*/
    stages.parallelStream().forEach(LayoutStage::act);

    for (LayoutStage stage : stages) {
      stage.draw();
    }
  }

  @Override
  public void dispose() {
    for (LayoutStage stage : stages) {
      stage.dispose();
    }
  }

  public void updateViewport(int width, int height) {
    layoutElements.get(0).set(0, 0, width, height);
  }

  public InputProcessor getInputProcessor() {
    return new InputMultiplexer(stages.toArray(new InputProcessor[0]));
  }
}

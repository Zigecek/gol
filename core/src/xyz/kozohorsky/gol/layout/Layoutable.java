package xyz.kozohorsky.gol.layout;

import xyz.kozohorsky.gol.stages.LayoutStage;

public interface Layoutable {
  void set (int x, int y, int width, int height);
  int getScaling();
}

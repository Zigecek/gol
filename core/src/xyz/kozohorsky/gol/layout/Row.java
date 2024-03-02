package xyz.kozohorsky.gol.layout;

import xyz.kozohorsky.gol.stages.LayoutStage;

public class Row extends LayoutElement implements Layoutable {

  public Row(int scaling, Layoutable... elements) {
    this.elements = elements;
    this.scaling = scaling;
  }

  public Row(Layoutable... elements) {
    // -1 = even
    this(-1, elements);
  }

  @Override
  public void set(int x, int y, int width, int height) {
    super.set(x, y, width, height);
    int totalScaling = 0;
    for (Layoutable element : elements) {
      if (element.getScaling() <= 0) {
        totalScaling = -1;
        break;
      } else {
        totalScaling += element.getScaling();
      }
    }
    if (totalScaling < 2) {
      totalScaling = elements.length;
    }
    System.out.println("Placing " + elements.length + " elements in a ROW");
    int currentX = x;
    for (Layoutable element : elements) {
      int elementWidth = width / totalScaling * (element.getScaling() > 0 ? element.getScaling() : 1);
      element.set(
        currentX,
        y,
        elementWidth,
        height
      );
      currentX += elementWidth;
    }
  }

  @Override
  public int getScaling() {
    return scaling;
  }
}

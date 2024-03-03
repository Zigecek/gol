package xyz.kozohorsky.gol.layout;

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
    int currentX = x;
    for (Layoutable element : elements) {
      int elementWidth = Math.round((float) width / totalScaling * (element.getScaling() > 0f ? element.getScaling() : 1f));
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

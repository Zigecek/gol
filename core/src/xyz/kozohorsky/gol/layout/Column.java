package xyz.kozohorsky.gol.layout;

public class Column extends LayoutElement implements Layoutable {

  public Column(int scaling, Layoutable... elements) {
    this.elements = elements;
    this.scaling = scaling;
  }

  public Column(Layoutable... elements) {
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
    int currentY = y;
    for (Layoutable element : elements) {
      int elementHeight = height / totalScaling * (element.getScaling() > 0 ? element.getScaling() : 1);
      element.set(
        x,
        currentY,
        width,
        elementHeight
      );
      currentY += elementHeight;
    }
  }

  @Override
  public int getScaling() {
    return scaling;
  }
}

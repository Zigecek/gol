package xyz.kozohorsky.gol.layout;

import java.util.Collections;

public abstract class LayoutElement implements Layoutable{

  Layoutable[] elements;
  int scaling;

  public Layoutable[] getElements() {
    return elements;
  }

  @Override
  public void set(int x, int y, int width, int height) {
    System.out.println("Dividing " + width + "x" + height + " space for " + elements.length + " elements");
  }
}

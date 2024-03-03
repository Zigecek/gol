package xyz.kozohorsky.gol.layout;

public abstract class LayoutElement implements Layoutable{

  Layoutable[] elements;
  int scaling;

  public Layoutable[] getElements() {
    return elements;
  }
}

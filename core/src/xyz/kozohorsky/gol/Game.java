package xyz.kozohorsky.gol;

import java.awt.*;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import static xyz.kozohorsky.gol.Gol.UNITS_PER_SIDE;

public class Game {
  private static Game instance;
  public HashSet<Point> aliveCells = new HashSet<Point>();
  public boolean isInEditMode = true;

  private Game() {
  }

  public static Game get() {
    if (instance == null) {
      instance = new Game();
      instance.fillRandom();
    }
    return instance;
  }

  public void nextGeneration() {
    if (isInEditMode) return;
    HashSet<Point> newAliveCells = new HashSet<>();
    HashSet<Point> checkedCells = new HashSet<>();
    for (Point cell : aliveCells) {
      int aliveNeighbours = countAliveNeighbours(cell.x, cell.y);
      if (aliveNeighbours < 2) {
        // 1. UNDERPOPULATION
        // not added to newAliveCells
      } else if (aliveNeighbours == 2 || aliveNeighbours == 3) {
        // 2. SURVIVAL
        newAliveCells.add(new Point(cell.x, cell.y));
      } else if (aliveNeighbours > 3) {
        // 3. OVERPOPULATION
        // not added to newAliveCells
      }

      // 4. REPRODUCTION
      forEachNeighbour(cell.x, cell.y, (neighbourX, neighbourY) -> {
        Point neighbour = new Point(neighbourX, neighbourY);
        if (checkedCells.contains(neighbour)) {
          return;
        }
        if (countAliveNeighbours(neighbourX, neighbourY) == 3) {
          newAliveCells.add(neighbour);
        }
        checkedCells.add(neighbour);
      });
    }
    aliveCells = newAliveCells;
  }

  private int countAliveNeighbours(int x, int y) {
    AtomicInteger count = new AtomicInteger(); // IDK what is this, IDE suggested it
    forEachNeighbour(x, y, (neighbourX, neighbourY) -> {
      if (aliveCells.contains(new Point(neighbourX, neighbourY))) {
        count.getAndIncrement();
      }
    });

    return count.get();
  }

  // method that takes method and the loop method for each neighbour
  private void forEachNeighbour(int x, int y, BiConsumer<Integer, Integer> method) {
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        if (i == 0 && j == 0) {
          // avoid the cell itself
          continue;
        }
        int neighbourX = x + i;
        int neighbourY = y + j;
        if (neighbourX >= 0 && neighbourX < UNITS_PER_SIDE && neighbourY >= 0 && neighbourY < UNITS_PER_SIDE) {
          // if the neighbour is within the bounds of the grid, call the method
          // TODO: make the grid infinite
          method.accept(neighbourX, neighbourY);
        }
      }
    }
  }

  public void fillRandom() {
    aliveCells.clear();
    for (int x = 0; x < UNITS_PER_SIDE; x++) {
      for (int y = 0; y < UNITS_PER_SIDE; y++) {
        if (Math.random() > 0.5) {
          aliveCells.add(new Point(x, y));
        }
      }
    }
  }

  public boolean isGenerationAlive() {
    return !aliveCells.isEmpty();
  }

  public void toggleCell(int x, int y) {
    Point point = new Point(x, y);
    if (isInEditMode) {
      if (aliveCells.contains(point)) {
        aliveCells.remove(point);
      } else {
        aliveCells.add(point);
      }
    }
  }

  public void setCellAlive(int x, int y) {
    aliveCells.add(new Point(x, y));
  }

  public void setCellDead(int x, int y, boolean killNeighbours) {
    aliveCells.remove(new Point(x, y));
    if (killNeighbours) {
      forEachNeighbour(x, y, (neighbourX, neighbourY) -> {
        aliveCells.remove(new Point(neighbourX, neighbourY));
      });
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int y = 0; y < UNITS_PER_SIDE; y++) {
      for (int x = 0; x < UNITS_PER_SIDE; x++) {
        sb.append(aliveCells.contains(new Point(x, y)) ? "O" : "X");
      }
      sb.append("\n");
    }
    return sb.toString();
  }
}

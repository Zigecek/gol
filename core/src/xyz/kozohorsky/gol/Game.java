package xyz.kozohorsky.gol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class Game {
  private static final boolean PARALLEL = false;
  private static Game instance;
  public HashSet<Point> aliveCells = new HashSet<Point>();
  public boolean isInEditMode = true;

  private Game() {
  }

  public static Game get() {
    if (instance == null) {
      instance = new Game();
    }
    return instance;
  }

  public void nextGeneration() {
    if (isInEditMode) return;

    System.out.println("Computing next generation of " + aliveCells.size() + " cells");

    Instant start = Instant.now();
    if (PARALLEL) {
      nextGenerationParallel();
    } else {
      nextGenerationSerial();
    }
    Instant ends = Instant.now();
    Duration duration = Duration.between(start, ends);
    printHumanDuration(duration);
  }

  private void nextGenerationSerial() {
    HashSet<Point> newAliveCells = new HashSet<>();
    HashSet<Point> checkedNeighbours = new HashSet<>();
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
        if (checkedNeighbours.contains(neighbour) || aliveCells.contains(neighbour)) {
          return;
        }
        if (countAliveNeighbours(neighbourX, neighbourY) == 3) {
          newAliveCells.add(neighbour);
        }
        checkedNeighbours.add(neighbour);
      });
    }
    aliveCells = newAliveCells;
  }

  private void nextGenerationParallel() {}

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
        method.accept(x + i, y + j);
      }
    }
  }

  public void fillRandom(Vector3 from, Vector3 to) {
    aliveCells.clear();
    for (int x = (int) from.x; x < (int) to.x; x++) {
      for (int y = (int) from.y; y < (int) to.y; y++) {
        if (Math.random() > 0.90) {
          aliveCells.add(new Point(x, y));
        }
      }
    }
    System.out.println("Filled with " + aliveCells.size() + " cells");
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

  public void setCellDead(int x, int y) {
    aliveCells.remove(new Point(x, y));
  }

  public void printHumanDuration(Duration duration) {
    String formattedElapsedTime = String.format("%02d days %02d h %02d min %02d s %d ms %d ns",
      duration.toDaysPart(),
      duration.toHoursPart(),
      duration.toMinutesPart(),
      duration.toSecondsPart(),
      duration.toMillisPart(),
      duration.toNanosPart() % 1000000L);
    System.out.println("Computing took: " + formattedElapsedTime);
  }
}

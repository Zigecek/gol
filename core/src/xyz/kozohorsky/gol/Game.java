package xyz.kozohorsky.gol;

import static xyz.kozohorsky.gol.Gol.UNITS_PER_SIDE;

public class Game {
  private static Game instance;
  public static GameUnit[][] gameUnits = new GameUnit[UNITS_PER_SIDE][UNITS_PER_SIDE];
  public boolean isInEditMode = true;
  private Game() {
  }

  public static Game get() {
    if (instance == null) {
      instance = new Game();
      instance.reset();
      instance.fillRandom();
    }
    return instance;
  }

  public void nextGeneration() {
    if (isInEditMode) return;
    GameUnit[][] newGameUnits = new GameUnit[UNITS_PER_SIDE][UNITS_PER_SIDE];
    for (int x = 0; x < UNITS_PER_SIDE; x++) {
      for (int y = 0; y < UNITS_PER_SIDE; y++) {
        newGameUnits[x][y] = new GameUnit();
        int aliveNeighbours = countAliveNeighbours(x, y);

        if (gameUnits[x][y].isAlive()) {
          if (aliveNeighbours < 2) {
            // 1. UNDERPOPULATION
            newGameUnits[x][y].die();
          } else if (aliveNeighbours == 2 || aliveNeighbours == 3) {
            // 2. SURVIVAL
            newGameUnits[x][y].born();
          } else if (aliveNeighbours > 3) {
            // 3. OVERPOPULATION
            newGameUnits[x][y].die();
          }
        } else {
          if (aliveNeighbours == 3) {
            // 4. REPRODUCTION
            newGameUnits[x][y].born();
          }
        }
      }
    }
    gameUnits = newGameUnits;
  }

  private int countAliveNeighbours(int x, int y) {
    int count = 0;
    for (int i = -1; i < 2; i++) {
      for (int j = -1; j < 2; j++) {
        int neighbourX = x + i;
        int neighbourY = y + j;
        if (i == 0 && j == 0) {
          continue;
        } else if (neighbourX < 0 || neighbourY < 0 || neighbourX >= UNITS_PER_SIDE || neighbourY >= UNITS_PER_SIDE) {
          continue;
        }
        if (gameUnits[neighbourX][neighbourY].isAlive()) {
          count++;
        }
      }
    }
    return count;
  }

  public void fillRandom() {
    for (int x = 0; x < UNITS_PER_SIDE; x++) {
      for (int y = 0; y < UNITS_PER_SIDE; y++) {
        if (Math.random() > 0.5) {
          gameUnits[x][y].born();
        }
      }
    }
  }

  public void reset() {
    for (int x = 0; x < UNITS_PER_SIDE; x++) {
      for (int y = 0; y < UNITS_PER_SIDE; y++) {
        gameUnits[x][y] = new GameUnit();
      }
    }
  }

  public boolean isGenerationAlive() {
    for (int x = 0; x < UNITS_PER_SIDE; x++) {
      for (int y = 0; y < UNITS_PER_SIDE; y++) {
        if (gameUnits[x][y].isAlive()) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isAlive(int x, int y) {
    return gameUnits[x][y].isAlive();
  }

  public void toggleUnit(int x, int y) {
    if (gameUnits[x][y].isAlive()) {
      gameUnits[x][y].die();
    } else {
      gameUnits[x][y].born();
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int y = 0; y < UNITS_PER_SIDE; y++) {
      for (int x = 0; x < UNITS_PER_SIDE; x++) {
        sb.append(gameUnits[x][y].isAlive() ? "O" : "X");
      }
      sb.append("\n");
    }
    return sb.toString();
  }
}

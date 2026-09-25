# Game Of Life

Conway's Game of Life, implemented with [libGDX](https://libgdx.com/). The world is
unbounded (cells live in a `HashSet` of coordinates), rendered with a desktop LWJGL3
window that is split into a game view and a configuration panel.

## Requirements

- **JDK 11–21** (the project targets Java 8 bytecode; the Gradle 8.5 wrapper does not
  support JDK 22+). Point `JAVA_HOME` at a supported JDK if your default is newer.
- No global Gradle install needed — use the bundled wrapper (`gradlew` / `gradlew.bat`).

## Run it (right after cloning)

From the repository root:

```bash
# Linux / macOS
./gradlew desktop:run

# Windows (PowerShell / cmd)
gradlew.bat desktop:run
```

The first run downloads the Gradle distribution and dependencies, so give it a minute.

### Build a runnable jar

```bash
./gradlew desktop:dist
# -> desktop/build/libs/Game Of Life-1.0.jar
java -jar "desktop/build/libs/Game Of Life-1.0.jar"
```

## Controls

Keyboard:

- **SPACE** — pause / resume the simulation (pausing enters *edit mode*)
- **N** — advance one generation manually
- **R** — randomize the cells (edit mode only)
- **C** — clear all cells (edit mode only)

Mouse:

- **Left click / drag** — bring cells to life (edit mode)
- **Right click / drag** — kill cells (edit mode)
- **Middle click / drag** — pan the camera
- **Scroll wheel** — zoom in / out

Configuration panel (right-hand side): a delay slider (controls the time between
generations) plus **Pause/Resume**, **Fill Random**, **Clear** and **New Generation**
buttons.

## Project layout

- `core/` — game logic, rendering stages and the layout system (platform-independent)
- `desktop/` — the LWJGL3 desktop launcher and Gradle run/dist tasks
- `assets/` — libGDX assets root and the working directory used by the run task

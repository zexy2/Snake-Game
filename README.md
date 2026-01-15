# Snake Game

Classic Snake game in Java Swing. Eat apples, grow longer, don't hit yourself or the walls.

## Running

Requires Java 11+.

```bash
# Compile
javac -d out src/snakegame/*.java

# Run
java -cp out snakegame.SnakeGame
```

## Controls

- Arrow keys or WASD to move
- Space to restart after game over

## Project Structure

```
src/snakegame/
├── SnakeGame.java    # Main entry point
├── GameFrame.java    # Window setup
├── GamePanel.java    # Game logic and rendering
└── AudioManager.java # Sound handling

assets/
├── images/           # apple.png, snake.png
└── sounds/           # background.wav, eat.wav, game_over.wav
```

## Notes

- Screen size: 1300x750, grid: 50px
- High score persists during session
- The rainbow snake body is intentional (random colors each frame)

## License

MIT

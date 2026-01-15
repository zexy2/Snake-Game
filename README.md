# 🐍 Snake Game

A classic Snake game built with Java Swing. Guide your snake to eat apples, grow longer, and try to beat your high score!

![Java](https://img.shields.io/badge/Java-11%2B-orange)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20macOS%20%7C%20Linux-blue)

## Features

- 🎮 Classic snake gameplay
- 🍎 Random apple spawning
- 📊 Score tracking with persistent high score
- 🎨 Colorful snake body with custom head sprite
- 🔊 Background music and sound effects
- ⌨️ Arrow keys and WASD controls
- 🔄 Instant restart with Space key

## Tech Stack

- **Language:** Java 11+
- **GUI Framework:** Swing
- **Audio:** Java Sound API

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 11 or higher

### Build & Run

```bash
# Clone the repository
git clone https://github.com/zexy2/Snake-Game.git
cd Snake-Game

# Compile
javac -d out src/snakegame/*.java

# Run
java -cp out snakegame.SnakeGame
```

## Controls

| Key | Action |
|-----|--------|
| ↑ / W | Move Up |
| ↓ / S | Move Down |
| ← / A | Move Left |
| → / D | Move Right |
| Space | Restart Game |

## Project Structure

```
snake-game/
├── src/
│   └── snakegame/
│       ├── SnakeGame.java      # Entry point
│       ├── GameFrame.java      # Window setup
│       ├── GamePanel.java      # Game logic & rendering
│       └── AudioManager.java   # Audio handling
├── assets/
│   ├── images/
│   │   ├── apple.png           # Apple sprite
│   │   └── snake.png           # Snake head sprite
│   └── sounds/
│       ├── background.wav      # Background music
│       ├── eat.wav             # Apple eaten sound
│       └── game_over.wav       # Game over sound
├── .gitignore
└── README.md
```

## Screenshots

*Coming soon*

## License

This project is open source and available under the MIT License.

## Contributing

Contributions are welcome! Feel free to:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

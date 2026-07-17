<div align="center">
  <img src="agent-resources/black-chip.png" alt="Play Poker logo" width="120" style="image-rendering: pixelated;">
  <h1>Play Poker</h1>
  <p><strong>A Minecraft 1.20.1 Forge mod that brings casino-style games to your world.</strong></p>
  <p>
    <a href="LICENSE">
      <img src="https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge" alt="License: MIT">
    </a>
    <img src="https://img.shields.io/badge/Minecraft-1.20.1-62b47a?style=for-the-badge" alt="Minecraft 1.20.1">
    <img src="https://img.shields.io/badge/Forge-47.4.10-e04e14?style=for-the-badge" alt="Forge 47.4.10">
    <img src="https://img.shields.io/badge/Java-17-blue?style=for-the-badge&logo=java&logoColor=white" alt="Java 17">
  </p>
</div>

---

## Table of Contents

- [About](#about)
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [Building from Source](#building-from-source)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

---

## About

**Play Poker** is a Minecraft Java Edition mod targeting **Minecraft 1.20.1** and built for the **Forge** loader. It aims to add polished casino mini-games — starting with **Blackjack** — using a mix of Java code, custom block entities, and datapack-inspired mechanics.

The project started from a multi-loader capable scaffold but is currently configured to build only for Forge. A complete reference datapack (`777_datapack`) is included under `agent-resources/` and was used as the blueprint for the Blackjack table behavior and visuals.

---

## Features

### 🃏 Blackjack Table

- Placeable `playpoker:blackjack_table` block with a horizontal `FACING` property oriented toward the player.
- Server-side game state stored in a `BlockEntity`.
- Datapack-style interaction using invisible `Interaction` entities for **Hit**, **Stand**, **Bet**, and **Ready**.
- Supports up to **3 simultaneous player hands** per table.
- Dealer draws until reaching **≥ 17**.
- Payouts:
  - Any player **21** pays **3×** the bet.
  - Normal win or dealer bust pays **2×** the bet.
  - Push returns the bet.
  - Loss or bust forfeits the bet.
- Visuals include a 3D table model, a villager dealer, `TextDisplay` cards, hand totals, and `ItemDisplay` bet items.

### 🎰 Poker Chip

- Creative-only `playpoker:poker_chip` item.
- Custom creative tab: **Play Poker**.
- 16×16 casino chip texture.

> 🎲 **Slot Machine** is planned as the next major feature, based on the same reference datapack.

---

## Requirements

| Component | Version |
|---|---|
| Minecraft | 1.20.1 |
| Forge Loader | 47.4.10 or compatible |
| Java | 17+ |

---

## Installation

1. Download the latest release jar from the Releases page (placeholder URL — replace once the repository is public).
2. Install **Minecraft Forge 1.20.1**.
3. Place the downloaded jar into your `.minecraft/mods` folder.
4. Launch the game and enjoy.

> **Note:** There is no official release yet. Build from source until the first release is published.

---

## Usage

### Crafting a Blackjack Table

A crafting recipe is provided under `data/playpoker/recipes/blackjack_table.json`. Craft and place the table in your world.

### Playing Blackjack

1. **Idle phase:**
   - Right-click the **Bet** interaction while holding an item to place a 1-item bet.
   - Left-click the **Bet** interaction to remove the bet.
   - Right-click the **Ready** interaction to toggle your seat as ready.
2. Once all occupied seats are ready, the round begins.
3. **Playing phase:**
   - Right-click **Hit** to draw another card.
   - Right-click **Stand** to end your turn.
4. The dealer plays automatically after all players stand or bust.
5. Winnings are paid out according to the rules above.

---

## Building from Source

### Prerequisites

- **Java 17**
- **Git**
- A working internet connection for Gradle dependencies

### Steps

```bash
# Clone the repository (replace with the real URL once available)
git clone https://github.com/YOUR_USERNAME/play-poker.git
cd play-poker

# Enter the mod project directory
cd playpoker-1.20.1

# Build the mod (gradlew is not executable by default)
bash gradlew build
```

### Common Gradle Tasks

| Task | Description |
|---|---|
| `bash gradlew build` | Build all active modules and produce the final jar. |
| `bash gradlew :forge:build` | Build only the Forge module. |
| `bash gradlew :forge:runClient` | Launch the Minecraft client with the mod. |
| `bash gradlew :forge:runServer` | Launch a dedicated server with the mod. |
| `bash gradlew projects` | List configured Gradle projects. |

### Output

The built Forge jar will be located at:

```
forge/build/libs/playpoker-forge-<version>-<minecraft_version>.jar
```

---

## Project Structure

```
play-poker/
├── agent-docs/            # Project documentation for Obsidian / agents
├── agent-resources/       # External assets and reference datapack
│   ├── 777_assets/
│   ├── 777_datapack/
│   └── black-chip.png
├── playpoker-1.20.1/      # Main mod project
│   ├── buildSrc/          # Gradle convention plugins
│   ├── common/            # Shared code and resources
│   ├── forge/             # Forge-specific module
│   ├── versionProperties/
│   ├── build.gradle
│   ├── settings.gradle
│   └── gradle.properties
└── LICENSE
```

---

## Contributing

Contributions are welcome. Please keep the following in mind:

1. Fork the repository and create a feature branch.
2. Follow the existing Java code style (`PascalCase` classes, `camelCase` members, `UPPER_SNAKE_CASE` constants).
3. Test your changes with `bash gradlew :forge:runClient` before submitting a pull request.
4. Update documentation if you change user-facing behavior.

---

## License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

---

## Acknowledgments

- The reference datapack and assets under `agent-resources/` were the basis for the Blackjack table visuals and logic.
- Built with [Unimined](https://github.com/unimined/unimined), [Mixin](https://github.com/SpongePowered/Mixin), and [Manifold](https://github.com/manifold-systems/manifold).

---

<p align="center">
  <strong>Made with ❤️ by rezzt.modz</strong>
</p>

# AGENTS.md — playpoker-1.20.1

> This file is written for AI coding agents. It assumes no prior knowledge of the project.  
> It replaces the previous empty `AGENTS.md`.

---

## Project Overview

`playpoker-1.20.1` is a Minecraft Java Edition mod project targeting **Minecraft 1.20.1**.

- **Mod ID:** `playpoker`
- **Maven group:** `dev.rezzt.playpoker`
- **Mod version:** `1.0.1`
- **Display name:** `play poker`
- **License:** MIT

The codebase is currently a minimal scaffold: a single shared `ExampleMod` class, one Mixin, and a Forge entrypoint. The actual gameplay content lives in a large pre-built datapack under `agent-resources/777_datapack/`, which implements slot-machine and blackjack mechanics using Minecraft commands (`.mcfunction` files), recipes, and advancements.

The project is configured as a **multi-loader-capable** template but is currently set to build only for **Forge**. Fabric and NeoForge loader modules can be added by changing `builds_for` in `versionProperties/1.20.1.properties`.

---

## Technology Stack

| Tool / Library | Purpose | Version / Notes |
|---|---|---|
| **Java** | Source & target language | 17 (`java_version=17`) |
| **Gradle** | Build tool | 8.14 (`gradle-wrapper.properties`) |
| **Groovy DSL** | Gradle build scripts | Used in root, modules, and `buildSrc` |
| **buildSrc** | Custom convention plugins | `buildSrc/src/main/groovy/` |
| **Unimined** | Minecraft dependency, mapping, and loader setup | `xyz.wagyourtail.unimined` `1.4.1-SNAPSHOT` |
| **Mojang mappings (`mojmap`)** | Deobfuscation strategy | Configured in `minecraft.gradle` |
| **Mixin** | Runtime bytecode injection | Config via `playpoker.mixins.json` |
| **Manifold** | Java preprocessor and string interpolation | `2025.1.14`; annotation processors `preprocessor` + `strings` |
| **JVMDowngrader** | Downgrade compiled bytecode to target Java | `1.3.0`, target Java 17 |
| **Forgix** | Merge loader-specific jars into one jar | `2.+`, `autoRun = true` in root `build.gradle` |
| **Forge** | Currently active mod loader | `47.4.10` (`forge_loader=47.4.10`) |

---

## Project Structure

```
playpoker-1.20.1/
├── buildSrc/                          # Gradle convention plugins
│   └── src/main/groovy/
│       ├── root.gradle                # Root-project plugin: preprocessor defs, JVMDowngrader wiring
│       ├── common.gradle              # Shared Java/Manifold/resource-expansion config
│       ├── minecraft.gradle           # Unimined Minecraft setup + common source/resource merging
│       ├── unimined-common.gradle     # Common module uses Fabric loader internally
│       ├── unimined-forge.gradle      # Forge-specific Unimined config
│       ├── unimined-fabric.gradle     # Fabric-specific config (inactive)
│       └── unimined-neoforge.gradle   # NeoForge-specific config (inactive)
│
├── common/                            # Shared code & resources
│   └── src/main/
│       ├── java/dev/rezzt/playpoker/
│       │   ├── ExampleMod.java        # Shared init, prints "Hello World!"
│       │   └── mixins/
│       │       └── MinecraftMixin.java # Injects into Minecraft#runTick
│       └── resources/
│           ├── pack.mcmeta            # Pack format 8
│           └── playpoker.mixins.json  # Mixin config
│
├── forge/                             # Forge loader module
│   └── src/main/
│       ├── java/dev/rezzt/playpoker/
│       │   └── ExampleModForge.java   # @Mod entrypoint
│       └── resources/META-INF/
│           └── mods.toml              # Forge mod metadata
│
├── versionProperties/                 # Per-MC-version configuration
│   └── 1.20.1.properties
│
├── agent-resources/                   # External assets (not wired into Gradle build yet)
│   ├── 777_datapack/                  # 793-file datapack: slot machine + blackjack
│   └── black-chip.png                 # 16×16 casino-chip icon
│
├── build.gradle                       # Root build: applies plugins incl. Forgix
├── settings.gradle                    # Loads version properties, includes common + loaders
├── gradle.properties                  # Mod metadata, Manifold version, Gradle flags
└── gradlew / gradlew.bat              # Gradle wrapper scripts
```

---

## Build System Details

### Key configuration files

1. **`gradle.properties`** — top-level constants:
   - `mc_ver=1.20.1`
   - `mod_name`, `mod_id`, `mod_version`, `group`, `license`
   - `manifold_version=2025.1.14`
   - Gradle performance flags (`-Xmx6G`, parallel, caching)

2. **`versionProperties/1.20.1.properties`** — per-MC-version toolchain and loader versions.

3. **`settings.gradle`** — reads the matching version file, validates `mc_ver`, exposes properties as `gradle.ext.*`, and includes `common` plus every loader listed in `builds_for`.

4. **`buildSrc` convention plugins** — centralize build logic:
   - `common.gradle` sets version = `${mod_version}-${minecraft_version}`, archive names, Manifold, resource expansion, and JVMDowngrader.
     It also configures `jvmdg.shadePath` to sanitize hyphens in the archive name, because the generated stub package must be a valid Java identifier.
   - `minecraft.gradle` applies Unimined, uses Mojang mappings, makes loader modules depend on `:common` and merge its sources/resources.
   - `root.gradle` writes `build.properties` preprocessor definitions and wires `remapJar → downgradeJar → shadeDowngradedApi`.

5. **Resource expansion** — the following files are processed with Gradle `expand`:
   - `pack.mcmeta`
   - `fabric.mod.json` (if present)
   - `META-INF/mods.toml`
   - `META-INF/neoforge.mods.toml` (if present)
   - `*.mixins.json`

   Available placeholders include `${version}`, `${mod_id}`, `${mod_name}`, `${mod_authors}`, `${license}`, `${description}`, `${minecraft_version}`, `${compatible_mc_versions}`, `${forge_loader}`, `${neoforge_loader}`, `${java_version}`, etc.

### Build lifecycle

1. `settings.gradle` loads `versionProperties/<mc_ver>.properties`.
2. `root.gradle` generates `build.properties` with preprocessor definitions like `MC_1_20_1=0` and `MC_VER=0`.
3. Each module compiles; loader modules merge `:common` sources/resources.
4. Unimined remaps the jar (`remapJar`).
5. JVMDowngrader downgrades bytecode to Java 17 (`downgradeJar` / `shadeDowngradedApi`).
6. Forgix merges the loader jars (`autoRun = true`).

---

## Build and Test Commands

> **Note:** `gradlew` is currently not executable. Use `bash gradlew <task>` or run `chmod +x gradlew` first.

### Common tasks

| Task | Description |
|---|---|
| `bash gradlew build` | Build all active modules and produce final jars. |
| `bash gradlew :forge:build` | Build only the Forge module. |
| `bash gradlew :common:build` | Build only the common module. |
| `bash gradlew :forge:runClient` | Launch the Minecraft client with the Forge mod. |
| `bash gradlew :forge:runServer` | Launch a dedicated server with the Forge mod. |
| `bash gradlew projects` | List configured Gradle projects. |

### Expected outputs

- `common/build/libs/playpoker-common-${mod_version}-${minecraft_version}.jar`
- `forge/build/libs/playpoker-forge-${mod_version}-${minecraft_version}.jar`
- (Forgix `mergeJars` is currently disabled because only Forge is active and the Forge jar already includes the common module.)

### Current build issue

Resolved. The following properties were added to `versionProperties/1.20.1.properties`:

- `neoforge_loader=47.1.105` — referenced by `common.gradle` for resource expansion.
- `fabric_loader=0.15.11` — referenced by `unimined-common.gradle` for the common module's internal Fabric-loader setup.

---

## Runtime Architecture

- The **common** module holds code intended to be shared across loaders.
- The **Forge** module's entrypoint is `ExampleModForge`, annotated with `@Mod(ExampleMod.MOD_ID)`. Its constructor calls `ExampleMod.init()`.
- `MinecraftMixin` injects at the head of `Minecraft.runTick` but no longer prints anything; it is kept as a boilerplate hook.
- Mixin is configured via `playpoker.mixins.json`; Forge registers that config via `mixinConfig("${mod_id}.mixins.json")` in `unimined-forge.gradle`.
- Run tasks (`runClient`, `runServer`) are set to `javaLauncher = null`, meaning they use the JVM that invoked Gradle rather than Gradle toolchains.

---

## Added Content

### `blackjack_table` block

Re-implemented to replicate the behavior and visuals of the 777 datapack blackjack table.
The implementation now uses the same entity-based interaction model as the datapack.

- **Block:** `playpoker:blackjack_table` — placeable Blackjack table with a
  horizontal `FACING` property oriented toward the player who placed it.
- **BlockEntity:** `BlackjackTableBlockEntity` stores the full game state and
  manages the visual entities spawned around the table.
- **Interaction (no GUI, datapack-style):**
  - Each seat has four `Interaction` entities: **Hit**, **Stand**, **Bet** and **Ready**.
  - **Idle phase:**
    - Right-click the Bet interaction while holding an item → place a 1-item bet.
    - Right-click the Ready interaction → toggle Ready for that seat.
    - Left-click the Bet interaction → remove/return the bet.
  - **Playing phase:**
    - Right-click the Hit interaction → Hit.
    - Right-click the Stand interaction → Stand.
  - Right-clicking the block itself acts as a fallback (Ready in idle, Hit in playing).
  - Breaking the table during a round forfeits bets and prints the datapack
    warning; breaking it while idle returns bets.
- **Gameplay (identical to the 777 datapack):**
  - Up to 3 simultaneous player hands per table.
  - All occupied seats must be Ready before the round starts.
  - Initial deal delivers 2 cards to each active seat and the dealer.
  - Dealer hole card is hidden until the dealer plays.
  - Dealer hits until reaching ≥ 17 (no soft-17 distinction).
  - Maximum 7 cards per hand.
  - Payouts: any player 21 pays 3×, normal win or dealer bust pays 2×,
    push returns the bet, loss/bust forfeits the bet.
- **Visuals (copied from the 777 resource pack):**
  - The exact `minecraft:models/item/blackjack_table.json` model and
    `minecraft:textures/item/blackjack_table.png` texture from
    `agent-resources/777_ASSETS/` are shipped with the mod.
  - `assets/minecraft/models/item/armor_stand.json` is overridden so an
    `armor_stand` item with `CustomModelData:6902` renders the 3D table model.
  - The placed block renders an `ItemDisplay` entity with the
    `armor_stand{CustomModelData:6902}` item, matching the datapack.
  - A villager dealer is spawned in front of the table.
  - `TextDisplay` entities show each card using the same Unicode glyphs as the
    datapack, with the same positions, scales, rotations and background colors.
  - `TextDisplay` entities show hand totals using mathematical bold digits.
  - `ItemDisplay` entities show the placed bet item.
  - Card deal/reveal uses the datapack's flip interpolation.
- **Assets:**
  - Block/item model and textures under `assets/playpoker/{models,textures}`
    and `assets/minecraft/{models,textures}` (armor_stand override).
  - Recipe, advancement and loot table under `data/playpoker/`.
- **Networking:** none — all state lives server-side in the block entity and
  visual updates are handled by real world entities.
- **Code locations:**
  - Shared logic: `common/src/main/java/dev/rezzt/playpoker/blackjack/`
  - Forge block/entity/visuals/interaction:
    `forge/src/main/java/dev/rezzt/playpoker/blackjack/`

### `poker_chip` item

- **Class:** `dev.rezzt.playpoker.item.PokerChipItem` (Forge module)
- **Registration:** `dev.rezzt.playpoker.ModItems.POKER_CHIP` (Forge `DeferredRegister`)
- **Creative tab:** custom tab `ModItems.PLAY_POKER_TAB` registered under `playpoker:play_poker_tab`
  - Title translation: `itemGroup.playpoker.play_poker_tab = "Play Poker"`
  - Tab icon: `Poker Chip` item
- **Behavior:**
  - No crafting recipe, loot, or trading — available only from the Creative inventory.
  - Can be dropped normally with `Q` or from the inventory.
- **Assets:**
  - Texture: `common/src/main/resources/assets/playpoker/textures/item/poker_chip.png` (copied from `agent-resources/black-chip.png`)
  - Model: `common/src/main/resources/assets/playpoker/models/item/poker_chip.json`
  - Translation: `common/src/main/resources/assets/playpoker/lang/en_us.json`

---

## Code Organization & Conventions

- **Package root:** `dev.rezzt.playpoker`
- **Shared classes:** place in `common/src/main/java/dev/rezzt/playpoker/`
- **Mixins:** place in `common/src/main/java/dev/rezzt/playpoker/mixins/` and register in `playpoker.mixins.json`
- **Loader entrypoints:** place in `<loader>/src/main/java/dev/rezzt/playpoker/`
- **Resources:**
  - Common resources go in `common/src/main/resources/`
  - Loader-specific resources go in `<loader>/src/main/resources/`
  - During build, loader modules copy common resources automatically.

### Style guidelines observed so far

- Java source uses standard Minecraft/Forge naming conventions (`PascalCase` classes, `camelCase` members).
- Constants are `UPPER_SNAKE_CASE` (e.g., `MOD_ID`).
- Comments and string literals are in English.
- No automated formatter (e.g., Spotless) is configured.

---

## Testing Strategy

There is **no automated test infrastructure** in this project:

- No `src/test` directories.
- No test frameworks (JUnit, Mockito, etc.) in dependencies.
- No CI configuration (no `.github/workflows`, `.gitlab-ci.yml`, etc.).

Testing is currently manual via the Gradle run tasks:

```bash
bash gradlew :forge:runClient
bash gradlew :forge:runServer
```

If you add automated tests, create `src/test/java/` in the relevant module and add the test framework to that module's `dependencies` block.

---

## Deployment / Release Process

- No CI/CD pipeline is present.
- Signing is disabled (`tasks.withType(Sign).configureEach { enabled = false }`).
- Module metadata generation is disabled (`GenerateModuleMetadata` tasks disabled).
- To produce a release artifact, run `bash gradlew build`.
- Forgix `autoRun` is currently disabled because only Forge is active and the Forge jar already contains the common module. Re-enable it in `build.gradle` if additional loaders are added.
- Before release, replace placeholder metadata in `gradle.properties`:
  - `mod_authors=["Your Name"]`
  - empty `description`
  - empty `credits`

---

## Agent Resources (External Content)

`agent-resources/` is **not currently wired into the Gradle build**. It contains:

- `777_datapack/` — a 793-file Minecraft datapack (770 `.mcfunction` files plus JSON recipes, advancements, tags, and pack metadata). It implements:
  - A fully rigged slot-machine using Animated Java (`custom_model_data: 6901`).
  - A complete blackjack table game with dealer logic, betting, hand-value calculation, card dealing, and chat feedback (`custom_model_data: 6902`).
  - Scoreboard objectives prefixed with `777.` and `777.blackjack.`.
- `black-chip.png` — a 16×16 PNG asset, likely intended as a casino-chip icon.

If the mod is meant to ship this datapack, you will need to copy it into a resource location under `common/src/main/resources/` or load it programmatically. If it is only reference material, it should be documented as such.

---

## Security Considerations

- **No secrets** are stored in the repository.
- **Mixin injection** currently runs on every client tick (`Minecraft.runTick`); this is fine for debugging but can affect performance or compatibility if left in production.
- **Placeholder mod metadata** should be replaced before distribution; the `mods.toml` currently has a permissive Forge dependency range (`[0,)`) and a `*` loader version, which may cause the mod to load on unsupported Forge versions.
- **Datapack commands** execute as server and may use `tellraw`, scoreboards, and entity tags. Review any new `.mcfunction` files for unintended side effects (e.g., `@a` selectors, excessive ticking loops) before enabling them on a server.
- **Generated `build.properties`** is gitignored but lives in the project root during build; do not commit it.

---

## Known Issues & Gotchas

1. ~~**Build configuration fails** until `neoforge_loader` and `fabric_loader` are added to `versionProperties/1.20.1.properties`.~~ **Resolved** — both loader properties are now present.
2. **`gradlew` is not executable** — use `bash gradlew` or `chmod +x gradlew`.
3. **No tests or CI** — all validation is manual. `compileTestJava` is disabled in `buildSrc/src/main/groovy/common.gradle` because the project has no tests and the Manifold javac plugin is not configured for the test source set.
4. **`ExampleMod` / `MinecraftMixin` are boilerplate** — they exist only to verify the toolchain.
5. **`agent-resources/777_datapack/` is not part of the built jar** — integration work is needed if it should be shipped.
6. **`common` uses the Fabric loader internally** even when only Forge is active. This is intentional in the Unimined template (it pulls in Mixin Extras), but the `fabric_loader` property must still be valid.
7. **Forgix `mergeJars` is disabled** (`autoRun = false` in root `build.gradle`) because only one loader is active and the Forge jar already includes common sources/resources.

---

## Quick Reference

- Target MC version: `1.20.1`
- Java version: `17`
- Active loader: `forge`
- Forge loader version: `47.4.10`
- Root package: `dev.rezzt.playpoker`
- Mixin config: `playpoker.mixins.json`
- Forge mod ID: `playpoker`
- Build command: `bash gradlew build`
- Run client: `bash gradlew :forge:runClient`

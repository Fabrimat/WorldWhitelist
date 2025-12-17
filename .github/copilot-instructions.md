# WorldWhitelist Copilot Instructions

## Project Overview
**WorldWhitelist** is a Spigot/Paper Minecraft plugin that implements per-world whitelist functionality. Players can be restricted from accessing specific worlds unless explicitly whitelisted. The plugin uses a YAML configuration for storing world and player UUID data.

## Architecture

### Core Components
1. **WorldWhitelist** (`WorldWhitelist.java`) - Plugin entry point
   - Singleton instance via `getInstance()`
   - Registers event listeners and command executors on enable
   - Loads config from `config.yml` using `saveDefaultConfig()`

2. **ConfigManager** (`config/ConfigManager.java`) - Single responsibility: config I/O
   - Stateless utility (new instance per call via `get()` static factory)
   - Always calls `plugin.saveConfig()` after mutations
   - Stores data in nested YAML: `users.{worldName}` and `whitelist_worlds` lists
   - Uses player UUIDs (not names) for storage; resolves to names when displaying

3. **Command Layer** (`commands/`) - Handles user input
   - `WhitelistCommand`: Main command with subcommands (on/off/add/remove/list)
   - `WhitelistReloadCommand`: Config reload
   - Tab completion respects command context (world names, player names, etc.)
   - All commands run async via `Bukkit.getScheduler().runTaskAsynchronously()`

4. **Event Listener** (`listeners/WorldListener.java`) - Enforces restrictions
   - Hooks `PlayerJoinEvent` and `PlayerChangedWorldEvent`
   - Teleports/kicks players trying to access non-default whitelisted worlds without permission
   - Uses async task scheduling for teleport operations

## Key Patterns & Conventions

### Data Storage
- **All player identifiers stored as UUIDs** (strings) in config YAML
- Config structure: `users.{worldName}` contains list of UUID strings
- Messages use indexed formatting: `String.format(message, param1, param2)`
- Config reload requires saving to disk immediately after each mutation

### Permission Model
- Format: `worldwhitelist.world.{worldName}.{action}` (e.g., `worldwhitelist.world.survival.add`)
- Special bypass permission: `worldwhitelist.{worldName}.bypass` - skips whitelist checks
- Reload permission: `worldwhitelist.admin.reload`

### Async Execution
- Long-running operations (config I/O) run async via `Bukkit.getScheduler().runTaskAsynchronously()`
- Teleport operations chain async → sync for consistency
- Commands return immediately (run async within)

## Build & Development

### Build
```bash
mvn clean package
```
- Maven Shade Plugin relocates PaperLib to `eu.k129.fabrimat.worldwhitelist.paperlib`
- Output: `target/WorldWhitelist-0.1-SNAPSHOT.jar`
- Requires Java 17+

### Configuration
- Plugin targets Spigot 1.16.5 (provided scope)
- Config filtering enabled - `${project.version}` replaced during build
- Place plugin JAR in server's `plugins/` folder; config auto-generated on first run

## Common Tasks

### Adding a New Command
1. Create class in `commands/` implementing `CommandExecutor` and optionally `TabCompleter`
2. Register in `WorldWhitelist.onEnable()`: `this.getCommand("name").setExecutor(new YourCommand())`
3. Add command metadata to `plugin.yml` with permissions and aliases

### Adding a New World Whitelist Feature
1. Add logic to `ConfigManager` for querying/mutating state
2. Always call `plugin.saveConfig()` after writes
3. Add message keys to `config.yml` for user feedback

### Modifying Permissions
- Edit `plugin.yml` to declare new permissions with defaults
- Check permissions in command handlers before executing actions
- Bypass permissions use dot notation (e.g., `worldwhitelist.world.{name}.bypass`)

## Files Reference
- **Plugin metadata**: [plugin.yml](src/main/resources/plugin.yml)
- **Default config**: [config.yml](src/main/resources/config.yml)
- **Build config**: [pom.xml](pom.xml)

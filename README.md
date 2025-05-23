# AreaCalls

**AreaCalls** is a Bukkit plugin for Minecraft that integrates with Discord to provide location-based voice channel switching. It lets you define **invisible spheres** in the game world—when a player walks into one, they're automatically moved to a corresponding Discord voice channel. Think of it as **proximity chat**, but with fully customizable call zones.

## 🔧 Features

- Create invisible, spherical areas in-game using commands
- Automatically move players to assigned Discord voice channels
- Flexible zone management (add/remove/list)
- Great for RPG servers, adventure maps, or immersive team play
- Works with any Discord bot token and server setup
- Everything is saved via config (areas, Minecraft-Player connections, etc.)

## 🧭 Commands

These commands are used in-game to interact with AreaCalls and configure voice channel spheres:

| Command | Description |
|--------|-------------|
| `/callpoint add <name> <radius> <voiceChannelId>` | Creates a new invisible sphere at your current location. Players who enter this sphere will be moved to the specified Discord voice channel. |
| `/callpoint remove <name>` | Deletes the specified sphere. |
| `/callpoint list` | Lists all current callpoints (invisible spheres). |
| `/verify <code>` | Links your Minecraft account to your Discord account using a code provided by the bot. Required for automatic voice movement. |
| `/unlink` | Unlinks your Minecraft account from your Discord account. |

## 🚀 Installation

1. Download the latest `AreaCalls.jar` from the [Releases](https://github.com/Checkmate-Chris1/AreaCalls/releases) page.
2. Drop it into your server’s `plugins` folder.
3. Restart or reload your server.
4. Configure your Discord bot keys in `plugins/AreaCalls/config.yml`.

## ⚙️ Configuration

Create a Discord bot and set the following in `config.yml`:

```yaml
bot-token: "YOUR_DISCORD_BOT_TOKEN"
```

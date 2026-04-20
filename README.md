# MC-pocket-discord-bridge

A minecraft Paper plugin that bridges Minecraft chat and actions like advancements to a Discord webhook.

## Features

- Chat messages forwarded to Discord
- Player join/leave events
- Death messages
- Advancement events


## Requirements

- Paper 1.19+
- Java 17+
- Maven
- A Discord webhook URL

## Building

1. Clone the repository
```bash
git clone https://github.com/yourusername/paper-discord-bridge.git
cd paper-discord-bridge
```
2. Build with Maven
```bash
mvn package
```
3. The compiled `.jar` will be in the `target/` folder

## Installation

1. Drop the `.jar` into your server's `plugins/` folder
2. Start the server once to generate the config
3. Open `plugins/MC-pocket-discord-bridge/config.yml`
4. Add your Discord webhook URL:
```yaml
webhook-url: "ur webhook url"
```
5. Restart the server

## Creating a Discord Webhook

1. Go to your Discord channel
2. Click **Edit Channel** → **Integrations** → **Webhooks**
3. Click **New Webhook**
4. Copy the webhook URL and paste it into `config.yml`

## Discord Bot

This plugin works alongside a Node.js Discord bot that handles the Discord → Minecraft direction, sending messages typed in Discord into the Minecraft chat via RCON.

repo: https://github.com/Hampter-0/MC-pocket

## License

MIT

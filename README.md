# OnlineDSConnect

Легковесный плагин для Minecraft (Paper), организующий асинхронный вывод сетевого статуса игрового сервера напрямую в голосовые каналы Discord.

## 🚀 Особенности
- **Zero TPS Impact:** Все сетевые запросы к Discord API (JDA) вынесены в асинхронные потоки и не блокируют основной поток сервера.
- **Динамический статус:** Автоматическое обновление плашек «Онлайн» / «Оффлайн» при старте и выключении сервера.
- **Гибкий таймер:** Обновление счётчика игроков по настраиваемому планировщику (каждые 5 минут) для исключения лимитов Discord (Rate Limits).

## 🛠 Технические требования
- **Ядро:** Paper / Purpur / Spigot `1.21.1` (Совместимо с протоколами `1.15 - 1.21.4`)
- **Java:** `Java 21` или выше
- **Сборщик:** `Apache Maven`

## ⚙️ Конфигурация (`config.toml`)
При первом запуске плагин генерирует дефолтный файл конфигурации в папке `plugins/OnlineDSConnect/config.toml`. 

```toml
bot-token = "YOUR_DISCORD_BOT_TOKEN"
guild-id = "YOUR_GUILD_ID"
status-channel-id = "YOUR_STATUS_CHANNEL_ID"
player-count-channel-id = "YOUR_PLAYER_COUNT_CHANNEL_ID"

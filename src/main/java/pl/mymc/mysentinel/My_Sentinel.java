package pl.mymc.mysentinel;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class My_Sentinel extends JavaPlugin implements Listener {

    private WordFilter wordFilter;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register("mysentinel", "Komenda pluginu My-Sentinel. Wpisz /mys help aby sprawdzic dostępne komendy", new My_SentinelCommand(this));
            commands.register("mys", "Komenda pluginu My-Sentinel. Wpisz /mys help aby sprawdzic dostępne komendy", new My_SentinelCommand(this));
        });
        List<String> bannedWords = getConfig().getStringList("bannedWords");
        this.wordFilter = new WordFilter(bannedWords);
        getServer().getPluginManager().registerEvents(this, this);
    }
    @Override
    public void onDisable() {
        AsyncPlayerChatEvent.getHandlerList().unregister((Plugin) this);
    }

    public void restartMySentinelTask() {
        try {
            AsyncPlayerChatEvent.getHandlerList().unregister((Plugin) this);
            super.reloadConfig();
            updateSentinel();
        } catch (Exception e) {
            getLogger().severe("Wystąpił błąd podczas przełądowania konfiguracji: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void updateSentinel(){
        List<String> bannedWords = getConfig().getStringList("bannedWords");
        this.wordFilter = new WordFilter(bannedWords);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        if (wordFilter.containsBannedWord(message)) {
            event.setMessage(wordFilter.censorMessage(message));
        }
    }
}

class WordFilter {

    private final List<String> bannedWords;

    public WordFilter(List<String> bannedWords) {
        this.bannedWords = bannedWords;
    }

    public boolean containsBannedWord(String message) {
        return bannedWords.stream().anyMatch(message::contains);
    }

    public String censorMessage(String message) {
        String[] words = message.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            for (String bannedWord : bannedWords) {
                if (words[i].toLowerCase().contains(bannedWord.toLowerCase())) {
                    String replacement = words[i].substring(0, 2) + "*".repeat(words[i].length() - 2);
                    words[i] = replacement;
                }
            }
        }
        return String.join(" ", words);
    }
}

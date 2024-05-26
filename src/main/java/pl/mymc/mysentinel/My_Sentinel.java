package pl.mymc.mysentinel;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class My_Sentinel extends JavaPlugin implements Listener {

    private WordFilter wordFilter;

    @Override
    public void onEnable() {
        saveDefaultConfig(); // Zapisuje domyślny plik konfiguracyjny, jeśli jeszcze nie istnieje
        List<String> bannedWords = getConfig().getStringList("bannedWords"); // Wczytuje listę zabronionych słów z pliku konfiguracyjnego
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
        for (String word : bannedWords) {
            if (message.toLowerCase().contains(word.toLowerCase())) {
                String replacement = word.substring(0, 2) + "*".repeat(word.length() - 2);
                message = message.replaceAll("(?i)" + word, replacement);
            }
        }
        return message;
    }

}

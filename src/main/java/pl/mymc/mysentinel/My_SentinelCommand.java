package pl.mymc.mysentinel;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public class My_SentinelCommand implements BasicCommand {
    private final My_Sentinel plugin;

    public My_SentinelCommand(My_Sentinel plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        PluginDescriptionFile pdf = plugin.getDescription();
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("help")) {
                if (stack.getSender().hasPermission("mysentinel.help")) {
                    stack.getSender().sendRichMessage("<gray>#######################################\n#\n#  <gold>Dostępne komendy dla My-Sentinel:\n<gray>#\n#  <gold>/mys help <gray>- <white>Wyświetla ten monit.\n<gray>#  <gold>/mys version <gray>- <white>Pokazuje info pluginu. \n<gray>#  <gold>/mys reload <gray>- <white>Przeładowuje plik konfiguracyjny\n<gray>#\n#######################################");
                } else {
                    stack.getSender().sendMessage("Nie masz uprawnień do tej komendy.");
                }
            } else if (args[0].equalsIgnoreCase("version")) {
                if (stack.getSender().hasPermission("mysentinel.version")) { //	getWebsite()
                    stack.getSender().sendRichMessage("<gray>#######################################\n#\n#   <gold>→ <bold>" + pdf.getName() + "</bold> ←\n<gray>#   <gold>Autor: <bold>" + pdf.getAuthors() + "</bold>\n<gray>#   <gold>WWW: <bold>" + pdf.getWebsite() + "</bold>\n<gray>#   <gold>Wersja: <bold>" + pdf.getVersion() + "</bold><gray>\n#\n#######################################");
                } else {
                    stack.getSender().sendRichMessage("Nie masz uprawnień do tej komendy.");
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (stack.getSender().hasPermission("mysentinel.reload")) {
                    plugin.reloadConfig();
                    //plugin.restartMySentinelTask();
                    stack.getSender().sendRichMessage("<green>Plik konfiguracyjny został przeładowany.</green>");
                } else {
                    stack.getSender().sendRichMessage("Nie masz uprawnień do tej komendy.");
                }
            }
        } else {
            stack.getSender().sendRichMessage("<blue>Komenda pluginu My-Sentinel. <green>Wpisz /mam help aby sprawdzic dostępne komendy");
        }
    }
}
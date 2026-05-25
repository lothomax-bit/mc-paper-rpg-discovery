package de.lothomax.geodiscovery.command;

import de.lothomax.geodiscovery.manager.ConfigManager;
import de.lothomax.geodiscovery.session.NamingSessionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TaufeCommand implements CommandExecutor {

    private final NamingSessionManager sessionManager;
    private final ConfigManager configManager;

    public TaufeCommand(NamingSessionManager sessionManager, ConfigManager configManager) {
        this.sessionManager = sessionManager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl nutzen.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("geodiscovery.player.taufe")) {
            player.sendMessage("§cDazu hast du keine Rechte.");
            return true;
        }

        if (!sessionManager.hasSession(player.getUniqueId())) {
            player.sendMessage("Du hast gerade keine Entdeckung zu benennen.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cBitte gib einen Namen an: /taufe <Name>");
            return true;
        }

        String name = String.join(" ", args);

        if (name.length() < 3) {
            String shortMsg = configManager.getMessage("naming-too-short");
            if (shortMsg != null) {
                player.sendMessage(shortMsg);
            } else {
                player.sendMessage("Der Name ist zu kurz (mindestens 3 Zeichen).");
            }
            return true;
        }

        if (name.length() > 48) {
            player.sendMessage("Der Name ist zu lang (maximal 48 Zeichen).");
            return true;
        }

        sessionManager.completeSession(player.getUniqueId(), name);

        return true;
    }
}
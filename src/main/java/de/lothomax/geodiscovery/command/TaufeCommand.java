package de.lothomax.geodiscovery.command;

import de.lothomax.geodiscovery.GeoDiscovery;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TaufeCommand implements CommandExecutor {

    private final GeoDiscovery plugin;

    public TaufeCommand(GeoDiscovery plugin) {
        this.plugin = plugin;
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

        if (args.length == 0) {
            player.sendMessage("§cBitte gib einen Namen an: /taufe <Name>");
            return true;
        }

        String name = String.join(" ", args);

        if (name.length() < 3) {
            player.sendMessage(plugin.getConfigManager().getMessage("naming-too-short"));
            return true;
        }

        if (!plugin.getNamingManager().hasActiveSession(player.getUniqueId())) {
            player.sendMessage("§cDu hast aktuell keine Region zu benennen.");
            return true;
        }

        plugin.getNamingManager().completeSession(player, name);

        return true;
    }
}

package me.nectarhoney.barstatus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BarCommand implements CommandExecutor {

    private BarStatus barStatus;

    BarCommand(BarStatus barStatus) {
        this.barStatus = barStatus;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be executed by players.");
            return true;
        }
        Player player = (Player) commandSender;
        if (!player.hasPermission("barstatus.toggle.bar") && !player.isOp()) {
            player.sendMessage("§cYou do not have the permission node §4barstatus.toggle.bar§c.");
            return true;
        }
        UUID uuid = player.getUniqueId();
        if (!barStatus.hasBarDisabled(uuid)) {
            barStatus.getPlayerAssociatedBossBar().get(uuid).removeAll();
            barStatus.getPlayerAssociatedBossBar().remove(uuid);
        }
        barStatus.toggleBar(uuid);
        player.sendMessage("§aYou have toggled the rank bar.");
        return true;
    }
}

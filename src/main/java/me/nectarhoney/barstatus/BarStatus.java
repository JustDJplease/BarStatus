package me.nectarhoney.barstatus;

// These are the imports. It tells the plugin which classes to get functions from.

import lombok.Getter;
import lombok.Setter;
import net.devhid.pexrankup.RankupPlugin;
import net.devhid.pexrankup.api.RankupAPI;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// This is the class header. There is only one class extending JavaPlugin. It is the main class.

public class BarStatus extends JavaPlugin {

    // This is a list of all bossbars, linked together with the player uuid's.
    // The uuid is an "ID" unique for every player. (Since players can change their name, I am not using their username!)
    @Getter @Setter public Map<UUID, BossBar> playerAssociatedBossBar;

    // This is the PEX-Rankup 'main' class. I store it here, so I can call it from a different class later.
    @Getter @Setter public RankupAPI rankupAPI;

    // This is the enable method. it is run upon start.
    @Override
    public void onEnable() {
        // Checking if Vault is installed.
        if (isPluginMissing("Vault")) {
            getLogger().severe("Missing dependency: Vault. This plugin will not work.");
            // Disabling the thing if it not.
            setEnabled(false);
            return;
        }
        // Same for pex-rankup.
        if (isPluginMissing("PEX-Rankup")) {
            getLogger().severe("Missing dependency: PEX-Rankup. This plugin will not work.");
            setEnabled(false);
            return;
        }
        // Assigning some classes to my @Getters at the top.
        setRankupAPI(RankupPlugin.getRankupAPI());
        setPlayerAssociatedBossBar(new HashMap<>());
        // Starting the 'runnable' (a task which is run again and again, every 5 ticks.)
        Bukkit.getScheduler().runTaskTimer(this, new BarTask(this), 5L, 5L);
    }

    // This is the disable method. It is run when the server unloads the plugin.
    @Override
    public void onDisable() {
        setPlayerAssociatedBossBar(null);
    }

    // My method to check if a plugin is missing
    private boolean isPluginMissing(String name) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        // If the plugin is not installed, it will be 'null'. This simply returns if a plugin is null or not.
        return plugin == null;
    }
}

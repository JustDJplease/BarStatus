package me.nectarhoney.barstatus;

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

public class BarStatus extends JavaPlugin {

    @Getter @Setter public Map<UUID, BossBar> playerAssociatedBossBar;
    @Getter @Setter public RankupAPI rankupAPI;

    @Override
    public void onEnable() {
        if (isPluginMissing("Vault")) {
            getLogger().severe("Missing dependency: Vault. This plugin will not work.");
            setEnabled(false);
            return;
        }
        if (isPluginMissing("PEX-Rankup")) {
            getLogger().severe("Missing dependency: PEX-Rankup. This plugin will not work.");
            setEnabled(false);
            return;
        }
        setRankupAPI(RankupPlugin.getRankupAPI());
        setPlayerAssociatedBossBar(new HashMap<>());
        Bukkit.getScheduler().runTaskTimer(this, new BarTask(this), 5L, 5L);
    }

    @Override
    public void onDisable() {
        setPlayerAssociatedBossBar(null);
    }

    private boolean isPluginMissing(String name) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        return plugin == null;
    }
}

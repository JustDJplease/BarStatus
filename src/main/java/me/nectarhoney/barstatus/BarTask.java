package me.nectarhoney.barstatus;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BarTask implements Runnable {

    private BarStatus barStatus;

    BarTask(BarStatus barStatus) {
        this.barStatus = barStatus;
    }

    @Override
    public void run() {
        Map<UUID, BossBar> bars = barStatus.getPlayerAssociatedBossBar();
        for (Player online : Bukkit.getOnlinePlayers()) {
            UUID uuid = online.getUniqueId();
            if (bars.containsKey(uuid)) {
                bars.put(uuid, BarUtil.update(barStatus, online, bars.get(uuid)));
                continue;
            }
            BossBar bar = BarUtil.update(barStatus, online, Bukkit.createBossBar("§fLoading data...", BarColor.GREEN, BarStyle.SEGMENTED_20));
            bars.put(uuid, bar);
            bar.addPlayer(online);
        }

        List<UUID> cleanup = new ArrayList<>();
        for (Map.Entry<UUID, BossBar> entry : bars.entrySet()) {
            UUID uuid = entry.getKey();
            if (Bukkit.getPlayer(uuid) == null) cleanup.add(uuid);
        }

        for (UUID uuid : cleanup) {
            BossBar bar = bars.get(uuid);
            bar.removeAll();
            bars.remove(uuid);
        }

        barStatus.setPlayerAssociatedBossBar(bars);
        cleanup.clear();
    }
}

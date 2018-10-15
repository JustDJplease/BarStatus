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

    // This is a 'constructor'. It is a link to the main class, so I can use main class methods in this class!
    private BarStatus barStatus;

    // Constructor part 2:
    BarTask(BarStatus barStatus) {
        this.barStatus = barStatus;
    }

    // This method is run every 5 ticks.
    @Override
    public void run() {
        // First we get the list of all bossbars & players
        Map<UUID, BossBar> bars = barStatus.getPlayerAssociatedBossBar();
        for (Player online : Bukkit.getOnlinePlayers()) {
            // then, for every online player:
            UUID uuid = online.getUniqueId();
            // we check if that list already contains a bossbar for them.
            if (bars.containsKey(uuid)) {
                // if it does, we simply update it.
                bars.put(uuid, BarUtil.update(barStatus, online, bars.get(uuid)));
                continue;
            }
            // if it doesn't, we make a new one.
            BossBar bar = BarUtil.update(barStatus, online, Bukkit.createBossBar("§fLoading data...", BarColor.GREEN, BarStyle.SEGMENTED_20));
            bars.put(uuid, bar);
            bar.addPlayer(online);
        }

        // Now, we're removing all bossbars of offline players.
        List<UUID> cleanup = new ArrayList<>();
        for (Map.Entry<UUID, BossBar> entry : bars.entrySet()) {
            UUID uuid = entry.getKey();
            if (Bukkit.getPlayer(uuid) == null) cleanup.add(uuid);
        }

        // and removing them from the bossbar list.
        for (UUID uuid : cleanup) {
            BossBar bar = bars.get(uuid);
            bar.removeAll();
            bars.remove(uuid);
        }

        // lastly, we update the list in the main class again!
        barStatus.setPlayerAssociatedBossBar(bars);
        cleanup.clear();
    }
}

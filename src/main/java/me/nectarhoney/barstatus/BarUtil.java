package me.nectarhoney.barstatus;

import net.devhid.pexrankup.api.RankupAPI;
import org.bukkit.ChatColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.math.BigDecimal;

class BarUtil {
    static BossBar update(BarStatus barStatus, Player player, BossBar bossBar) {
        bossBar.setProgress(calculateProgress(barStatus, player));
        bossBar.setTitle(formatTitle(barStatus, player));
        return bossBar;
    }

    private static String formatTitle(BarStatus barStatus, Player player) {
        RankupAPI api = barStatus.getRankupAPI();
        PermissionUser permissionUser = PermissionsEx.getUser(player);
        String currentGroup = api.getCurrentGroup(permissionUser);
        if (currentGroup == null || currentGroup.equals("")) currentGroup = "Unranked";

        String color = "§7";
        switch (ChatColor.stripColor(currentGroup.toLowerCase())) {
            case "unranked":
                break;
            case "0":
            case "4":
            case "8":
            case "12":
            case "16":
                color = "§4";
                break;
            case "20":
            case "24":
            case "28":
            case "32":
            case "36":
                color = "§a";
                break;
            case "40":
            case "44":
            case "48":
            case "52":
            case "56":
                color = "§5";
                break;
            case "60":
            case "64":
            case "68":
            case "72":
            case "76":
                color = "§b";
                break;
            case "80":
            case "84":
            case "88":
            case "92":
            case "96":
                color = "§8";
                break;
            case "100":
                color = "§d";
                break;
            default:
                break;
        }
        String currentStatus = api.getBalanceFormatted(player);
        if (currentStatus == null || currentStatus.equals("")) currentStatus = "0.0";

        String nextStatus = api.getCostOfNextRankFormatted(permissionUser);
        if (nextStatus == null || nextStatus.equals("")) nextStatus = "0.0";

        return "§f§lRank: " + color + "§l" + currentGroup + " §f§lNext: §f" + currentStatus + "§7/§f" + nextStatus;
    }

    private static double calculateProgress(BarStatus barStatus, Player player) {
        RankupAPI api = barStatus.getRankupAPI();
        PermissionUser permissionUser = PermissionsEx.getUser(player);
        BigDecimal current = api.getBalance(player);
        if (current == null) return 0.0D;

        BigDecimal next = api.getCostOfNextRank(permissionUser);
        if (next == null) return 0.0D;

        double ratio = current.doubleValue() / next.doubleValue();
        if (Double.isNaN(ratio)) return 0.0D;
        if (ratio < 0.0) return 0.0D;
        if (ratio > 1.0) return 1.0D;
        return ratio;
    }
}

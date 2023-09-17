package me.hxli.openjoinmessage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        if (player.hasPlayedBefore()) {
            e.setJoinMessage(ChatColor.DARK_GRAY + "[" +
                    ChatColor.DARK_AQUA + "+" +
                    ChatColor.DARK_GRAY + "] " +
                    ChatColor.BOLD + ChatColor.GRAY + player.getDisplayName());
        } else {
            e.setJoinMessage(ChatColor.GREEN + "Welcome " +
                    ChatColor.BOLD + ChatColor.GRAY + player.getDisplayName() +
                    ChatColor.GREEN + " to " +
                    ChatColor.LIGHT_PURPLE + "Minecraft Server" +
                    ChatColor.GREEN + "!");
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

        Player player = e.getPlayer();

        e.setQuitMessage(ChatColor.DARK_GRAY + "[" +
                ChatColor.RED + "-" +
                ChatColor.DARK_GRAY + "] " +
                ChatColor.BOLD + ChatColor.GRAY + player.getDisplayName());

    }
}

package me.hxli.openjoinmessage.listeners;

import me.hxli.openjoinmessage.OpenJoinMessage;
import me.hxli.openjoinmessage.database.DataBaseUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    private boolean usingDefaultConfig;

    public JoinLeaveListener(boolean usingDefaultConfig) {
        this.usingDefaultConfig = usingDefaultConfig;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        if (this.usingDefaultConfig) {
            player.sendMessage("§7[§bO§3J§9M§7] §aTo properly use the plugin you will need to edit §3ojmdb_config.json§a in the plugins folder.");
        } else {
            if (player.hasPlayedBefore()) {
                e.setJoinMessage(String.format(OpenJoinMessage.msgs.get("join"), e.getPlayer().getDisplayName()));
            } else {
                e.setJoinMessage(String.format(OpenJoinMessage.msgs.get("welcome"), e.getPlayer().getDisplayName()));
            }
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

        if (!this.usingDefaultConfig) {
            Player player = e.getPlayer();

            e.setQuitMessage(String.format(OpenJoinMessage.msgs.get("leave"), e.getPlayer().getDisplayName()));
        }
    }
}

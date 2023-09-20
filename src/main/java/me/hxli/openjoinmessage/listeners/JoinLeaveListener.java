package me.hxli.openjoinmessage.listeners;

import me.hxli.openjoinmessage.OpenJoinMessage;
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
            e.setJoinMessage(String.format(OpenJoinMessage.msgs.get("join"), e.getPlayer().getDisplayName()));
        } else {
            e.setJoinMessage(String.format(OpenJoinMessage.msgs.get("welcome"), e.getPlayer().getDisplayName()));
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

        Player player = e.getPlayer();

        e.setQuitMessage(String.format(OpenJoinMessage.msgs.get("leave"), e.getPlayer().getDisplayName()));

    }
}

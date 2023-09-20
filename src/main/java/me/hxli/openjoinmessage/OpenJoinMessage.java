package me.hxli.openjoinmessage;

import me.hxli.openjoinmessage.commands.ojmCommand;
import me.hxli.openjoinmessage.database.DataBaseUtils;
import me.hxli.openjoinmessage.listeners.JoinLeaveListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Logger;

public final class OpenJoinMessage extends JavaPlugin {

    public static HashMap<String, String> msgs = new HashMap<String, String>();

    @Override
    public void onEnable() {

        DataBaseUtils dataBase = new DataBaseUtils();
        Logger logger = this.getLogger();
        logger.info("[OJM] Database connected");

        checkValues(dataBase);
        logger.info("[OJM] Values read");

        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);
        logger.info("[OJM] Listeners registered");


        this.getCommand("ojm").setExecutor(new ojmCommand(dataBase));
        logger.info("[OJM] Commands registered");

        logger.info("[OJM] Plugin ready!");
    }

    public static void checkValues(DataBaseUtils db) {
        if (db.readValueMessage("welcome")==null) {
            msgs.put("welcome", "§bWelcome §r%s §bto the server");
            db.createValueMessage("welcome", msgs.get("welcome"));
        } else {
            msgs.put("welcome",db.readValueMessage("welcome"));
        }

        if (db.readValueMessage("join")==null) {
            msgs.put("join", "§8[§2+§8] §r%s");
            db.createValueMessage("join", msgs.get("join"));
        } else {
            msgs.put("join",db.readValueMessage("join"));
        }

        if (db.readValueMessage("leave")==null) {
            msgs.put("leave", "§8[§c-§8] §r%s");
            db.createValueMessage("leave", msgs.get("leave"));
        } else {
            msgs.put("leave",db.readValueMessage("leave"));
        }
    }
}

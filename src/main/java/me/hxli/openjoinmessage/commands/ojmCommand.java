package me.hxli.openjoinmessage.commands;

import me.hxli.openjoinmessage.database.DataBaseUtils;
import me.hxli.openjoinmessage.OpenJoinMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ojmCommand implements CommandExecutor {

    private DataBaseUtils db;
    private final String ojmTitle = "§3O§bJ§3M";
    private String newValue = "";
    private boolean dbExists;

    public ojmCommand(DataBaseUtils db, boolean dbExists) {
        this.db = db;
        this.dbExists = dbExists;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (strings.length == 0) {
                player.sendMessage("§7[" + ojmTitle + "§7] §aCurrently using §6§lOpenJoinMessage§r §3v0.1.0");
                return false;
            } else if (strings[0].equals("help") || strings[0].equals("?")) {
                player.sendMessage(
                        "§8╔ §l§7--["+ojmTitle+"§7]--\n" +
                           "§8║ §6/ojm help\n" +
                           "§8║ §6/ojm edit §e<welcome/join/leave> <message>\n" +
                           "§8║ §6/ojm view §e<welcome/join/leave>\n" +
                           "§8╚ §l§7--------\n"
                );
            } else if (!dbExists) {
                player.sendMessage("§7[" + ojmTitle + "§7] §cCouldn't load database.");
            } else if (
                    strings[0].equals("edit") &&
                            strings.length>=3 &&
                            (
                                strings[1].equals("welcome") ||
                                strings[1].equals("join") ||
                                strings[1].equals("leave")
                            )
            ) {
                String value = "";

                for (int i = 2; i < strings.length; i++) { // You can basically type the custom message without using ""
                    value += strings[i] + " ";
                }

                newValue = value.replace("&", "§");

                OpenJoinMessage.msgs.put(strings[1], newValue);
                db.updateValueMessage(strings[1], newValue);

                player.sendMessage(
                        "§7["+ojmTitle+"§7] §aUpdated successfully.\n"+
                                "§7["+ojmTitle+"§7] §aType: §e"+strings[1]+" §aMessage: §r"+newValue.replace("%s", "user")+"§a."
                );
            } else if (
                    strings[0].equals("view") &&
                            strings.length>=2 &&
                            (
                                    strings[1].equals("welcome") ||
                                            strings[1].equals("join") ||
                                            strings[1].equals("leave")
                            )
            ) {

                String viewValue = "";
                viewValue = OpenJoinMessage.msgs.get(strings[1]).replace("%s", "user");

                player.sendMessage("§7["+ojmTitle+"§7] §6"+strings[1]+" message: \n§7["+ojmTitle+"§7] "+viewValue);

            } else {
                return false;
            }

            return true;
        }

        return false;
    }
}

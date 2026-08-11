package com.bost.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class BostPlugin extends JavaPlugin {

    private Map<String, InetSocketAddress> servers = new HashMap<>();

    @Override
    public void onEnable() {
        servers.put("survival", new InetSocketAddress("127.0.0.1", 15544));
        servers.put("rpg", new InetSocketAddress("127.0.0.1", 15545));

        getLogger().info("BOST NETWORK GATEWAY (TRANSFER MODE) INITIALIZED");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("bust") && args.length >= 2) {
            if (args[0].equalsIgnoreCase("join")) {

                String target = args[1].toLowerCase();

                if (servers.containsKey(target)) {
                    player.sendMessage("§bПеренаправляю на " + target + "...");

                    // Исправление: передаем хост и порт отдельно
                    InetSocketAddress address = servers.get(target);
                    player.transfer(address.getHostString(), address.getPort());
                } else {
                    player.sendMessage("§cСервер не найден!");
                }

                return true;
            }
        }

        return false;
    }
}

package com.bost.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BostPlugin extends JavaPlugin {
    private Map<String, String> servers = new HashMap<>();

    @Override
    public void onEnable() {
        servers.put("survival", "localhost:15544");
        servers.put("rpg", "localhost:15545");
        
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getLogger().info("====================================");
        getLogger().info("BOST NETWORK GATEWAY INITIALIZED (UNIVERSAL)");
        getLogger().info("Connected to " + servers.size() + " nodes.");
        getLogger().info("====================================");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (label.equalsIgnoreCase("bust")) {
            if (args.length < 1) {
                player.sendMessage("§cUsage: /bust <join|transfer> <server> [amount]");
                return true;
            }

            if (args[0].equalsIgnoreCase("join")) {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /bust join <server>");
                    return true;
                }
                String targetServer = args[1];
                if (!servers.containsKey(targetServer)) {
                    player.sendMessage("§cServer not found!");
                    return true;
                }
                player.sendMessage("§bConnecting to " + targetServer + "...");
                sendPlayerToServer(player, targetServer);
                return true;
            }
            return true;
        }
        return false;
    }

    private void sendPlayerToServer(Player player, String serverName) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(serverName);
        } catch (IOException e) {
            getLogger().severe("Could not send connect packet: " + e.getMessage());
        }
        player.sendPluginMessage(this, "BungeeCord", b.toByteArray());
    }
}

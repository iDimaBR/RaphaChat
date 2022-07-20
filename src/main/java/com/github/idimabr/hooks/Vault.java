package com.github.idimabr.hooks;

import com.github.idimabr.RaphaChat;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Vault {
    private Chat chat;

    public Vault(RaphaChat plugin) {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager.getPlugin("Vault") == null) {
            pluginManager.disablePlugin(plugin);
            return;
        }

        RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        this.chat = rsp.getProvider();
    }

    public Chat getChat() {
        return chat;
    }
}

package com.github.idimabr.commands;

import com.github.idimabr.RaphaChat;
import com.github.idimabr.controller.ChannelController;
import com.github.idimabr.controller.DataController;
import com.github.idimabr.model.Channel;
import com.github.idimabr.model.PlayerData;
import com.github.idimabr.storage.dao.StorageRepository;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class ChatCommand implements CommandExecutor {

    private RaphaChat plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("§cOnly players can execute this.");
            return false;
        }

        final Player player = (Player) sender;
        final ChannelController channelController = plugin.getChannelController();
        final DataController dataController = plugin.getDataController();
        final StorageRepository repository = plugin.getRepository();

        if(args.length == 1){
            final String targetChannel = args[0];
            final Channel channel = channelController.getChannel(targetChannel.toLowerCase());
            if(channel == null){
                player.sendMessage("§cChannel '" + targetChannel + "' not found.");
                return false;
            }

            final PlayerData data = dataController.getData(player);
            if(data == null){
                player.sendMessage("§cCan't not found your default channel to apply changes...");
                return false;
            }

            repository.changeChannel(player, channel.getName());
            data.setChannel(channel);
            player.sendMessage("§aYour channel has been changed to '" + channel.getName() + "'.");
            return true;
        }else{
            player.sendMessage("§cUse /chat <name> to change your default channel");
            return false;
        }
    }
}
